package com.example.vidyaksha.data.mapper

import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.data.local.Slide
import com.example.vidyaksha.data.local.SlideBlock
import com.example.vidyaksha.presentation.common.CardItem

fun Slide.toCardItem(): CardItem {

    val firstText =
        blocks.firstNotNullOfOrNull { block ->
            when (block) {
                is SlideBlock.Text -> block.text
                is SlideBlock.Markdown -> block.markdown
                else -> null
            }
        } ?: "Tap to read more"

    val firstImage =
        blocks.firstNotNullOfOrNull { block ->
            when (block) {
                is SlideBlock.Image -> block.images.firstOrNull()
                else -> null
            }
        }

    return CardItem(
        imageRes = ContentMapper.imageRes(firstImage),
        heading = title,
        description = firstText
    )
}
