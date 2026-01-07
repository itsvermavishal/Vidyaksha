package com.example.vidyaksha.data.local
sealed class SlideBlock {

    data class Text(
        val text: String
    ) : SlideBlock()

    data class Markdown(
        val markdown: String
    ) : SlideBlock()

    data class Image(
        val images: List<String>
    ) : SlideBlock()

    data class Table(
        val headers: List<String>,
        val rows: List<List<String>>
    ) : SlideBlock()
}

