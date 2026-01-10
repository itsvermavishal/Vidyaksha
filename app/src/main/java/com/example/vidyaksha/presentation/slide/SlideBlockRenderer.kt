package com.example.vidyaksha.presentation.slide

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.data.local.SlideBlock

@Composable
fun SlideBlockRenderer(
    blocks: List<SlideBlock>
) {
    blocks.forEach { block ->
        when (block) {

            is SlideBlock.Text -> {
                Text(
                    text = block.text,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            is SlideBlock.Markdown -> {
                Text(
                    text = block.markdown.replace("###", "").trim(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            is SlideBlock.Image -> {
                block.images.forEach { img ->
                    Image(
                        painter = painterResource(
                            id = ContentMapper.imageRes(img)
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            is SlideBlock.Table -> {
                // Future enhancement
            }
        }
    }
}
