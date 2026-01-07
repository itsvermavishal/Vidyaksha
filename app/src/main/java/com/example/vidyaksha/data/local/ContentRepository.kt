package com.example.vidyaksha.data.local

interface ContentRepository {

    fun loadContent(): AppContent

    fun getModules(): List<Module>

    fun getModule(id: Int): Module

    fun getLevel(moduleId: Int, levelId: Int): Level

    fun getChapter(
        moduleId: Int,
        level: LevelType,
        chapterId: Int
    ): Chapter
}
