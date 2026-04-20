package org.hubcitydev.nor4x4

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hubcitydev.nor4x4.timer.TimerViewModel
import org.hubcitydev.nor4x4.ui.CustomConfigScreen
import org.hubcitydev.nor4x4.ui.StartScreen
import org.hubcitydev.nor4x4.ui.TimerScreen
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    private val timerViewModel: TimerViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permissions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkAndRequestPermissions()

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                
                val isWorkoutActive = timerViewModel.isWorkoutActive.collectAsState().value
                val startDest = if (isWorkoutActive) "timer" else "start"
                
                NavHost(
                    navController = navController,
                    startDestination = startDest
                ) {
                    composable("start") {
                        StartScreen(
                            onStandardClick = { config ->
                                timerViewModel.setConfig(config)
                                timerViewModel.setWorkoutActive(true)
                                navController.navigate("timer") { popUpTo(0) }
                            },
                            onCustomClick = {
                                navController.navigate("custom_config")
                            }
                        )
                    }
                    composable("custom_config") {
                        val initialConfig = timerViewModel.getSavedCustomConfig()
                        CustomConfigScreen(
                            initialConfig = initialConfig,
                            onStartClick = { config ->
                                timerViewModel.saveCustomConfig(config)
                                timerViewModel.setConfig(config)
                                timerViewModel.setWorkoutActive(true)
                                navController.navigate("timer") { popUpTo(0) }
                            }
                        )
                    }
                    composable("timer") {
                        TimerScreen(
                            viewModel = timerViewModel,
                            onResetClick = {
                                timerViewModel.setWorkoutActive(false)
                                navController.navigate("start") { popUpTo(0) }
                            }
                        )
                    }
                }
            }
        }
    }
    
    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BODY_SENSORS)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }
}
