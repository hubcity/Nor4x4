package org.hubcitydev.nor4x4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import org.hubcitydev.nor4x4.timer.TimerPhase
import org.hubcitydev.nor4x4.timer.TimerViewModel

@Composable
fun TimerScreen(viewModel: TimerViewModel, onResetClick: () -> Unit) {
    val currentPhase by viewModel.currentPhase.collectAsState()
    val timeLeft by viewModel.timeLeftInPhase.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val heartRate by viewModel.heartRate.collectAsState()
    val maxHeartRate by viewModel.maxHeartRate.collectAsState()
    val minHeartRate by viewModel.minHeartRate.collectAsState()

    val phaseColor = when (currentPhase) {
        is TimerPhase.Interval -> Color.Red
        is TimerPhase.Recovery -> Color.Green
        is TimerPhase.WarmUp -> Color.Yellow
        is TimerPhase.CoolDown -> Color.Cyan
        else -> MaterialTheme.colors.primary
    }

    val totalDuration = currentPhase.durationSeconds.toFloat()
    val progress = if (totalDuration > 0) (totalDuration - timeLeft) / totalDuration else 1f

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    
    val displayTime = if (currentPhase == TimerPhase.Finished) "HR Range" else String.format("%02d:%02d", minutes, seconds)
    val displayHr = if (currentPhase == TimerPhase.Finished) "${minHeartRate.toInt()}-${maxHeartRate.toInt()}" else heartRate.toInt().toString()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (currentPhase != TimerPhase.Finished) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxSize().padding(10.dp),
                indicatorColor = phaseColor,
                trackColor = phaseColor.copy(alpha = 0.2f),
                strokeWidth = 6.dp
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentPhase.displayName,
                style = MaterialTheme.typography.caption1,
                color = phaseColor,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = displayTime,
                style = MaterialTheme.typography.display3,
                modifier = Modifier.semantics {
                    contentDescription = if (currentPhase == TimerPhase.Finished) {
                        "Heart Rate Range"
                    } else {
                        "$minutes minutes and $seconds seconds remaining"
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "Heart rate $displayHr beats per minute"
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = displayHr,
                    style = MaterialTheme.typography.display3
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.toggleTimer() },
                    modifier = Modifier.size(ButtonDefaults.SmallButtonSize)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause timer" else "Start timer"
                    )
                }

                if (!isRunning) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            viewModel.resetTimer()
                            onResetClick()
                        },
                        modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                        colors = ButtonDefaults.secondaryButtonColors()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset timer"
                        )
                    }
                }
            }
        }
    }
}
