package com.example.vidyaksha.presentation.slides

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.ContentRepository
import com.example.vidyaksha.data.local.LevelType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SlideViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    fun getSlides(
        moduleId: Int,
        level: LevelType,
        chapterId: Int
    ) = repository.getChapter(moduleId, level, chapterId).slides
}

