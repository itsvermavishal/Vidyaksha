package com.example.vidyaksha.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.vidyaksha.domain.model.Session
import com.example.vidyaksha.domain.model.Task

sealed class DashboardEvent{
    data object SaveSubject: DashboardEvent()

    data object DeleteSubject: DashboardEvent()

    data class OnDeleteSessionButtonClick(val session: Session): DashboardEvent()

    data class onTaskIsCompleteChange(val task: Task): DashboardEvent()

    data class OnSubjectCardColorChange(val color: List<Color>): DashboardEvent()

    data class OnSubjectNameChange(val subjectName: String): DashboardEvent()

    data class OnGoalStudyHoursChange(val hours: String): DashboardEvent()
}
