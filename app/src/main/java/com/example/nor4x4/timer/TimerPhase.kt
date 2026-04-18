package com.example.nor4x4.timer

sealed class TimerPhase(val displayName: String, val durationSeconds: Int) {
    class WarmUp(durationSeconds: Int) : TimerPhase("Warm Up", durationSeconds)
    class Interval(val index: Int, val total: Int, durationSeconds: Int) : TimerPhase("Interval $index/$total", durationSeconds)
    class Recovery(val index: Int, val total: Int, durationSeconds: Int) : TimerPhase("Recovery $index/$total", durationSeconds)
    class CoolDown(durationSeconds: Int) : TimerPhase("Cool Down", durationSeconds)
    object Finished : TimerPhase("Finished", 0)
}
