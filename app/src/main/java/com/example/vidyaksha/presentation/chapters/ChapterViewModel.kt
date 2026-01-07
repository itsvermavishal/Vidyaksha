package com.example.vidyaksha.presentation.chapters

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    fun getChapters(moduleId: Int, levelId: Int) =
        repository.getLevel(moduleId, levelId).chapters
}
