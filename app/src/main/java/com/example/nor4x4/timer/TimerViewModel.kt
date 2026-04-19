package com.example.nor4x4.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow
import com.example.nor4x4.timer.WorkoutConfig

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
