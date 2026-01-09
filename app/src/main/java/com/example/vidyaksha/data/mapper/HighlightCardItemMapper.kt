package com.example.vidyaksha.data.mapper

import com.example.vidyaksha.data.local.HighlightSlide
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.presentation.common.CardItem

fun HighlightSlide.toCardItem(): CardItem {
    return CardItem(
        imageRes = ContentMapper.imageRes(image),
        heading = title,
        description = description ?: "Tap to read more"
    )
}

