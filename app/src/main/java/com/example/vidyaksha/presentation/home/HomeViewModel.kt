package com.example.vidyaksha.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidyaksha.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    subjectRepository: SubjectRepository
) : ViewModel() {

    // Expose subjects as a hot Flow<State>. Adjust initial value and mapping to your domain model.
    val subjects = subjectRepository
        .getAllSubjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Add more UI state / events here as needed.
}