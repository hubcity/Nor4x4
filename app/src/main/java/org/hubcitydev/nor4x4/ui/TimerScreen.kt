package org.hubcitydev.nor4x4.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.wear.compose.material.Icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import org.hubcitydev.nor4x4.timer.TimerPhase
import org.hubcitydev.nor4x4.timer.TimerViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color

@Composable
fun TimerScreen(viewModel: TimerViewModel, onResetClick: () -> Unit) {
    val currentPhase by viewModel.currentPhase.collectAsState()
    val timeLeft by viewModel.timeLeftInPhase.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val heartRate by viewModel.heartRate.collectAsState()
    val maxHeartRate by viewModel.maxHeartRate.collectAsState()
    val minHeartRate by viewModel.minHeartRate.collectAsState()

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    
    val displayTime = if (currentPhase == TimerPhase.Finished) "HR Range" else String.format("%02d:%02d", minutes, seconds)
    val displayHr = if (currentPhase == TimerPhase.Finished) "${minHeartRate.toInt()}-${maxHeartRate.toInt()}" else heartRate.toInt().toString()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!isRunning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .background(Color.DarkGray)
                    .semantics { contentDescription = "Reset timer" }
                    .clickable(role = Role.Button) {
                        viewModel.resetTimer()
                        onResetClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Reset", color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else {
            Spacer(modifier = Modifier.fillMaxWidth().weight(0.2f))
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(0.6f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentPhase.displayName,
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colors.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = displayTime,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = if (currentPhase == TimerPhase.Finished) {
                        "Heart Rate Range"
                    } else {
                        "$minutes minutes and $seconds seconds remaining"
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = "Heart rate $displayHr beats per minute"
                }
            ) {
                Text(
                    text = "$displayHr",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .background(MaterialTheme.colors.primary)
                .semantics { contentDescription = if (isRunning) "Pause timer" else "Start timer" }
                .clickable(role = Role.Button) { viewModel.toggleTimer() },
            contentAlignment = Alignment.Center
        ) {
            Text(if (isRunning) "Pause" else "Start", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
