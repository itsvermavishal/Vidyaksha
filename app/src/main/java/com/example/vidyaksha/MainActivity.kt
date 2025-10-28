package com.example.vidyaksha

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.core.app.ActivityCompat
import com.example.vidyaksha.presentation.NavGraphs
import com.example.vidyaksha.presentation.destinations.SessionScreenRouteDestination
import com.example.vidyaksha.presentation.session.StudySessionTimerService
import com.example.vidyaksha.presentation.theme.VidyakshaTheme
import com.example.vidyaksha.navigation.BottomNavItems
import com.example.vidyaksha.navigation.RoundedFloatingBottomBar
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VidyakshaTheme {
                // Always set content (previous code only set content if isBound)
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        RoundedFloatingBottomBar(navController = navController, items = BottomNavItems)
                    },
                    content = { innerPadding: PaddingValues ->
                        Surface(modifier = Modifier.padding(innerPadding)) {
                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                navController = navController,
                                dependenciesContainerBuilder = {
                                    // Register the timerService only when bound. (Same pattern you used.)
                                    if (isBound) {
                                        dependency(SessionScreenRouteDestination) { timerService }
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

