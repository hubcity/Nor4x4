package org.hubcitydev.nor4x4.timer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutConfig(
    val reps: Int = 4,
    val warmupSeconds: Int = 10 * 60,
    val intervalSeconds: Int = 4 * 60,
    val recoverySeconds: Int = 3 * 60,
    val cooldownSeconds: Int = 5 * 60
) : Parcelable
