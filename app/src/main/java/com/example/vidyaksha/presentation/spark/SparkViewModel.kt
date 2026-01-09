package com.example.vidyaksha.presentation.spark

import androidx.lifecycle.ViewModel
import com.example.vidyaksha.data.local.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SparkViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    val modules = repository.getModules()

    fun getMarketTrends() =
        repository.loadContent()
            .highlights.first { it.id == "market_trends" }
            ?.slides
            ?: emptyList()

    fun getTopGainers() =
        repository.loadContent()
            .highlights.first { it.id == "top_gainers" }
            ?.slides
            ?: emptyList()

    fun getLatestNews() =
        repository.loadContent()
            .highlights.first { it.id == "latest_news" }
            ?.slides
            ?: emptyList()


}

