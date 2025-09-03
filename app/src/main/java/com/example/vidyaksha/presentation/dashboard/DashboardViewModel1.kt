package com.example.vidyaksha.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DashboardViewModel1 @Inject constructor(
    private val subjectRepository: SubjectRepository
): ViewModel() {

}