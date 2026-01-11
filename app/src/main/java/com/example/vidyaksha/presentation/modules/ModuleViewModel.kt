package com.example.vidyaksha.presentation.modules

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.ContentRepository
import com.example.vidyaksha.data.local.LearningLevelProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject   // ✅ CORRECT IMPORT

@HiltViewModel
class ModuleViewModel @Inject constructor(
    private val repository: ContentRepository   // ✅ make it a property
) : ViewModel() {

    fun getModules() = repository.getModules()

    fun getModule(moduleId: Int) = repository.getModule(moduleId)

    fun calculateLevelProgress(
        learningProgress: LearningLevelProgress
    ): Float {
        val totalSlides = learningProgress.chapters.sumOf { it.slides.size }
        if (totalSlides == 0) return 0f

        val completedSlides = learningProgress.chapters.sumOf { chapter ->
            chapter.slides.count { it.isCompleted }
        }

        return completedSlides.toFloat() / totalSlides
    }

}
