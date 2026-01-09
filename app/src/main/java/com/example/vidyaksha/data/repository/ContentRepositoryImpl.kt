package com.example.vidyaksha.data.repository

import android.content.Context
import com.example.vidyaksha.data.local.AppContent
import com.example.vidyaksha.data.local.Chapter
import com.example.vidyaksha.data.local.ContentRepository
import com.example.vidyaksha.data.local.Level
import com.example.vidyaksha.data.local.LevelType
import com.example.vidyaksha.data.local.LevelTypeAdapter
import com.example.vidyaksha.data.local.Module
import com.example.vidyaksha.data.local.SlideBlock
import com.example.vidyaksha.presentation.slides.SlideBlockAdapter
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContentRepository {

    private val gson = GsonBuilder()
        .registerTypeAdapter(SlideBlock::class.java, SlideBlockAdapter())
        .registerTypeAdapter(LevelType::class.java, LevelTypeAdapter())
        .create()


    private var cached: AppContent? = null

    override fun loadContent(): AppContent {
        if (cached != null) return cached!!

        return try {
            val json = context.assets
                .open("content.json")
                .bufferedReader()
                .use { it.readText() }

            android.util.Log.d("CONTENT", "✅ content.json loaded successfully")
            android.util.Log.d("CONTENT", json.take(300)) // first 300 chars

            val content = gson.fromJson(json, AppContent::class.java)
                ?: throw IllegalStateException("Parsed AppContent is null")

            cached = content
            content
        } catch (e: Exception) {
            android.util.Log.e("CONTENT", "❌ FAILED TO LOAD OR PARSE content.json", e)
            throw e
        }
    }

    override fun getModules(): List<Module> {
        return loadContent().modules
    }

    override fun getModule(id: Int): Module {
        return loadContent().modules.first { it.id == id }
    }

    override fun getLevel(moduleId: Int, levelId: Int): Level {
        return getModule(moduleId)
            .levels.first { it.id == levelId }
    }

    override fun getChapter(
        moduleId: Int,
        level: LevelType,
        chapterId: Int
    ): Chapter {
        return getModule(moduleId)
            .levels.first { it.name == level }
            .chapters.first { it.id == chapterId }
    }
}
