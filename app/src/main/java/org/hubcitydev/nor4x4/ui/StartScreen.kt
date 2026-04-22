package org.hubcitydev.nor4x4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import org.hubcitydev.nor4x4.timer.WorkoutConfig
import androidx.wear.compose.material.Chip
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription

@Composable
fun StartScreen(
    onStandardClick: (WorkoutConfig) -> Unit,
    onCustomClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isLoading = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.semantics { contentDescription = "Loading..." }
            )
        } else {
            Text(text = "Nor 4x4", style = MaterialTheme.typography.title2)
            Spacer(modifier = Modifier.height(16.dp))
            Chip(
                onClick = { 
                    isLoading = true
                    onStandardClick(WorkoutConfig()) 
                },
                label = { Text("Standard") },
                secondaryLabel = { Text("4x4 default") },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Chip(
                onClick = { 
                    isLoading = true
                    onCustomClick() 
                },
                label = { Text("Custom") },
                secondaryLabel = { Text("Adjust times") },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            )
        }
    }
}
