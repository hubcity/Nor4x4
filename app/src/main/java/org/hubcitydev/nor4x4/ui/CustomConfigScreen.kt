package org.hubcitydev.nor4x4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.MaterialTheme
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

    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Text("Custom Setup", style = MaterialTheme.typography.title3, modifier = Modifier.padding(bottom = 8.dp)) }
        
        item { ConfigRow("Reps", reps.toString()) { reps = (reps + it).coerceAtLeast(1) } }
        item { ConfigRow("Warmup", formatTime(warmupSeconds)) { warmupSeconds = (warmupSeconds + it * 30).coerceAtLeast(0) } }
        item { ConfigRow("Interval", formatTime(intervalSeconds)) { intervalSeconds = (intervalSeconds + it * 30).coerceAtLeast(30) } }
        item { ConfigRow("Recovery", formatTime(recoverySeconds)) { recoverySeconds = (recoverySeconds + it * 30).coerceAtLeast(0) } }
        item { ConfigRow("Cooldown", formatTime(cooldownSeconds)) { cooldownSeconds = (cooldownSeconds + it * 30).coerceAtLeast(0) } }
        
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

@Composable
private fun ConfigRow(label: String, value: String, onChange: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.caption1)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onChange(-1) }, modifier = Modifier.semantics { contentDescription = "Decrease $label" }) { Text("-") }
            Spacer(modifier = Modifier.width(8.dp))
            Text(value, style = MaterialTheme.typography.body1, modifier = Modifier.width(50.dp), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onChange(1) }, modifier = Modifier.semantics { contentDescription = "Increase $label" }) { Text("+") }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
