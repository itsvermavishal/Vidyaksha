package com.example.vidyaksha.presentation.modules

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject   // ✅ CORRECT IMPORT

@HiltViewModel
class ModuleViewModel @Inject constructor(
    private val repository: ContentRepository   // ✅ make it a property
) : ViewModel() {

    fun getModules() = repository.getModules()

    fun getModule(moduleId: Int) = repository.getModule(moduleId)
}
