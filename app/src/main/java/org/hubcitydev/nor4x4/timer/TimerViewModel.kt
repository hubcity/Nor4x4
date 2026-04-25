package org.hubcitydev.nor4x4.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.hubcitydev.nor4x4.timer.WorkoutConfig

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    
    init {
        TimerManager.initialize(application)
    }

    val currentPhase: StateFlow<TimerPhase> = TimerManager.currentPhase
    val timeLeftInPhase: StateFlow<Int> = TimerManager.timeLeftInPhase
    val isRunning: StateFlow<Boolean> = TimerManager.isRunning
    val heartRate: StateFlow<Double> = TimerManager.heartRate
    val maxHeartRate: StateFlow<Double> = TimerManager.maxHeartRate
    val minHeartRate: StateFlow<Double> = TimerManager.minHeartRate
    val isWorkoutActive: StateFlow<Boolean> = TimerManager.isWorkoutActive

    val displayTime: StateFlow<String> = combine(currentPhase, timeLeftInPhase) { phase, timeLeft ->
        if (phase == TimerPhase.Finished) {
            "HR Range"
        } else {
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60

            val sb = StringBuilder(5)
            if (minutes < 10) sb.append('0')
            sb.append(minutes)
            sb.append(':')
            if (seconds < 10) sb.append('0')
            sb.append(seconds)
            sb.toString()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00")

    val displayHr: StateFlow<String> = combine(currentPhase, heartRate, minHeartRate, maxHeartRate) { phase, hr, minHr, maxHr ->
        if (phase == TimerPhase.Finished) {
            "${minHr.toInt()}-${maxHr.toInt()}"
        } else {
            hr.toInt().toString()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0")

    fun setConfig(newConfig: WorkoutConfig) {
        TimerManager.setConfig(newConfig)
    }

    fun toggleTimer() {
        TimerManager.toggleTimer()
    }

    fun resetTimer() {
        TimerManager.resetTimer()
    }

    fun setWorkoutActive(active: Boolean) {
        TimerManager.setWorkoutActive(active)
    }

    private val prefs = application.getSharedPreferences("Nor4x4Prefs", android.content.Context.MODE_PRIVATE)

    fun getSavedCustomConfig(): WorkoutConfig {
        return WorkoutConfig(
            reps = prefs.getInt("reps", 4),
            warmupSeconds = prefs.getInt("warmup", 10 * 60),
            intervalSeconds = prefs.getInt("interval", 4 * 60),
            recoverySeconds = prefs.getInt("recovery", 3 * 60),
            cooldownSeconds = prefs.getInt("cooldown", 5 * 60)
        )
    }

    fun saveCustomConfig(config: WorkoutConfig) {
        prefs.edit()
            .putInt("reps", config.reps)
            .putInt("warmup", config.warmupSeconds)
            .putInt("interval", config.intervalSeconds)
            .putInt("recovery", config.recoverySeconds)
            .putInt("cooldown", config.cooldownSeconds)
            .apply()
    }
}
