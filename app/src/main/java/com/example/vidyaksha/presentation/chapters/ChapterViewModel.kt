package com.example.vidyaksha.presentation.chapters

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.Chapter
import com.example.vidyaksha.data.local.ChapterProgressUI
import com.example.vidyaksha.data.local.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {
    fun getModule(moduleId: Int) =
        repository.getModule(moduleId)

    fun getLevel(moduleId: Int, levelId: Int) =
        repository.getLevel(moduleId, levelId)

    fun buildChapterProgress(chapter: Chapter): ChapterProgressUI {
        val total = chapter.slides.size

        // later replace with DB / Firebase value
        val completed = 0

        return ChapterProgressUI(
            totalSlides = total,
            completedSlides = completed
        )
    }

}
