package org.hubcitydev.nor4x4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import org.hubcitydev.nor4x4.timer.WorkoutConfig

@Composable
fun CustomConfigScreen(
    initialConfig: WorkoutConfig,
    onStartClick: (WorkoutConfig) -> Unit
) {
    var reps by remember { mutableStateOf(initialConfig.reps) }
    var warmupSeconds by remember { mutableStateOf(initialConfig.warmupSeconds) }
    var intervalSeconds by remember { mutableStateOf(initialConfig.intervalSeconds) }
    var recoverySeconds by remember { mutableStateOf(initialConfig.recoverySeconds) }
    var cooldownSeconds by remember { mutableStateOf(initialConfig.cooldownSeconds) }

    val listState = rememberScalingLazyListState()

    Scaffold(
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(
                    "Custom Setup",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            item { ConfigRow("Reps", reps.toString(), valueDescription = "$reps repetitions", decreaseEnabled = reps > 1) { reps = (reps + it).coerceAtLeast(1) } }
            item { ConfigRow("Warmup", formatTime(warmupSeconds), valueDescription = formatTimeDescription(warmupSeconds), decreaseEnabled = warmupSeconds > 0) { warmupSeconds = (warmupSeconds + it * 30).coerceAtLeast(0) } }
            item { ConfigRow("Interval", formatTime(intervalSeconds), valueDescription = formatTimeDescription(intervalSeconds), decreaseEnabled = intervalSeconds > 30) { intervalSeconds = (intervalSeconds + it * 30).coerceAtLeast(30) } }
            item { ConfigRow("Recovery", formatTime(recoverySeconds), valueDescription = formatTimeDescription(recoverySeconds), decreaseEnabled = recoverySeconds > 0) { recoverySeconds = (recoverySeconds + it * 30).coerceAtLeast(0) } }
            item { ConfigRow("Cooldown", formatTime(cooldownSeconds), valueDescription = formatTimeDescription(cooldownSeconds), decreaseEnabled = cooldownSeconds > 0) { cooldownSeconds = (cooldownSeconds + it * 30).coerceAtLeast(0) } }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Chip(
                    onClick = {
                        onStartClick(
                            WorkoutConfig(reps, warmupSeconds, intervalSeconds, recoverySeconds, cooldownSeconds)
                        )
                    },
                    modifier = Modifier.padding(bottom = 32.dp),
                    label = { Text("Start") }
                )
            }
        }
    }
}

@Composable
private fun ConfigRow(label: String, value: String, valueDescription: String = value, decreaseEnabled: Boolean = true, onChange: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.caption2, color = MaterialTheme.colors.secondary)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Button(
                onClick = { onChange(-1) },
                modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                colors = ButtonDefaults.secondaryButtonColors(),
                enabled = decreaseEnabled
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease $label")
            }
            
            Text(
                value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .width(60.dp)
                    .clearAndSetSemantics { contentDescription = valueDescription },
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = { onChange(1) },
                modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
                colors = ButtonDefaults.secondaryButtonColors()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase $label")
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

private fun formatTimeDescription(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "$m minutes and $s seconds"
}
