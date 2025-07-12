package com.example.vidyaksha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vidyaksha.presentation.dashboard.DashboardScreen
import com.example.vidyaksha.presentation.theme.StudyboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyboardTheme {
                DashboardScreen()
            }
        }
    }
}
