package org.hubcitydev.nor4x4.timer

import android.content.Context
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.media.RingtoneManager
import android.content.Intent
import org.hubcitydev.nor4x4.health.HealthServicesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

object TimerManager {
    private var healthServicesManager: HealthServicesManager? = null
    private var vibrator: Vibrator? = null
    private var applicationContext: Context? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var cachedVibrationEffect: VibrationEffect? = null
    private var cachedRingtone: android.media.Ringtone? = null

    private var phases: List<TimerPhase> = listOf(TimerPhase.Finished)
    private var currentPhaseIndex = 0

    private val _currentPhase = MutableStateFlow<TimerPhase>(TimerPhase.Finished)
    val currentPhase: StateFlow<TimerPhase> = _currentPhase.asStateFlow()

    private val _timeLeftInPhase = MutableStateFlow(0)
    val timeLeftInPhase: StateFlow<Int> = _timeLeftInPhase.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _heartRate = MutableStateFlow(0.0)
    val heartRate: StateFlow<Double> = _heartRate.asStateFlow()

    private val _maxHeartRate = MutableStateFlow(0.0)
    val maxHeartRate: StateFlow<Double> = _maxHeartRate.asStateFlow()

    private val _minHeartRate = MutableStateFlow(0.0)
    val minHeartRate: StateFlow<Double> = _minHeartRate.asStateFlow()

    private var timerJob: Job? = null
    private var healthJob: Job? = null
    private var config: WorkoutConfig = WorkoutConfig()

    private val scope = CoroutineScope(Dispatchers.Default)

    fun initialize(context: Context) {
        if (applicationContext == null) {
            applicationContext = context.applicationContext
            healthServicesManager = HealthServicesManager(context.applicationContext)
            vibrator = context.getSystemService(Vibrator::class.java)
            
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nor4x4:TimerWakeLock")

            val timings = longArrayOf(0, 250, 250, 500, 250, 750)
            val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
            cachedVibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, -1)

            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                cachedRingtone = RingtoneManager.getRingtone(applicationContext, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setConfig(newConfig: WorkoutConfig) {
        this.config = newConfig
        generatePhases()
        resetTimer()
    }

    fun setWorkoutActive(active: Boolean) {
        _isWorkoutActive.value = active
    }

    private fun generatePhases() {
        val newPhases = mutableListOf<TimerPhase>()
        if (config.warmupSeconds > 0) {
            newPhases.add(TimerPhase.WarmUp(config.warmupSeconds))
        }
        for (i in 1..config.reps) {
            if (config.intervalSeconds > 0) {
                newPhases.add(TimerPhase.Interval(i, config.reps, config.intervalSeconds))
            }
            if (i < config.reps && config.recoverySeconds > 0) {
                newPhases.add(TimerPhase.Recovery(i, config.reps - 1, config.recoverySeconds))
            }
        }
        if (config.cooldownSeconds > 0) {
            newPhases.add(TimerPhase.CoolDown(config.cooldownSeconds))
        }
        newPhases.add(TimerPhase.Finished)
        phases = newPhases
    }

    fun toggleTimer() {
        if (_isRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        if (_currentPhase.value == TimerPhase.Finished) {
            resetTimer()
        }
        
        _isRunning.value = true
        
        applicationContext?.let { ctx ->
            val intent = Intent(ctx, TimerService::class.java)
            ctx.startForegroundService(intent)
        }
        
        wakeLock?.acquire()

        timerJob = scope.launch {
            while (_isRunning.value && _timeLeftInPhase.value > 0) {
                delay(1000)
                _timeLeftInPhase.value -= 1
                
                if (_timeLeftInPhase.value == 0) {
                    moveToNextPhase()
                }
            }
        }
        
        startHealthMonitoring()
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        healthJob?.cancel()
        
        applicationContext?.let { ctx ->
            val intent = Intent(ctx, TimerService::class.java)
            ctx.stopService(intent)
        }
        
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
    }

    fun resetTimer() {
        pauseTimer()
        if (phases.isNotEmpty()) {
            currentPhaseIndex = 0
            val initialPhase = phases[currentPhaseIndex]
            _currentPhase.value = initialPhase
            _timeLeftInPhase.value = initialPhase.durationSeconds
        }
        _heartRate.value = 0.0
        _maxHeartRate.value = 0.0
        _minHeartRate.value = 0.0
    }

    private fun moveToNextPhase() {
        currentPhaseIndex++
        if (currentPhaseIndex < phases.size) {
            val nextPhase = phases[currentPhaseIndex]
            _currentPhase.value = nextPhase
            _timeLeftInPhase.value = nextPhase.durationSeconds
            triggerFeedback()
            
            if (nextPhase == TimerPhase.Finished) {
                pauseTimer()
            }
        }
    }

    private fun triggerFeedback() {
        if (vibrator?.hasVibrator() == true) {
            val effect = cachedVibrationEffect ?: run {
                val timings = longArrayOf(0, 250, 250, 500, 250, 750)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
                VibrationEffect.createWaveform(timings, amplitudes, -1).also {
                    cachedVibrationEffect = it
                }
            }
            vibrator?.vibrate(effect)
        }
        
        try {
            val r = cachedRingtone ?: run {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                applicationContext?.let { ctx ->
                    RingtoneManager.getRingtone(ctx, notification).also {
                        cachedRingtone = it
                    }
                }
            }

            r?.let { ringtone ->
                ringtone.play()
                scope.launch {
                    delay(3000)
                    ringtone.stop()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startHealthMonitoring() {
        healthJob?.cancel()
        healthJob = scope.launch {
            healthServicesManager?.heartRateMeasureFlow()
                ?.catch { e -> e.printStackTrace() }
                ?.collect { hr ->
                    _heartRate.value = hr
                    if (hr > _maxHeartRate.value) {
                        _maxHeartRate.value = hr
                    }
                    if (_minHeartRate.value == 0.0 || hr < _minHeartRate.value) {
                        _minHeartRate.value = hr
                    }
                }
        }
    }
}
