package com.example.vidyaksha.presentation.session

import android.content.Context
import android.content.Intent

object ServiceHelper {

    fun triggerForegroundService(context: Context, message: String) {
        Intent(context, StudySessionTimerService::class.java).apply {
            this.action = message
            context.startService(this)

        }
    }
}