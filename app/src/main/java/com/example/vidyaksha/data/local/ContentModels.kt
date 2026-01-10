package com.example.vidyaksha.data.local

/* ---------- ROOT ---------- */
data class AppContent(
    val version: Int,
    val modules: List<Module>,
    val highlights: List<HighlightSection> = emptyList()
)
/* ---------- TOP CAROUSEL CONTENT ---------- */
data class HighlightSection(
    val id: String,
    val title: String,
    val slides: List<Slide>
)


/* ---------- MODULE ---------- */
/*
Shown in Spark grid
Top card in Level screen uses:
- id
- title
- description
*/
data class Module(
    val id: Int,
    val title: String,
    val image: String,
    val description: String = "",
    val levels: List<Level>
)

/* ---------- LEVEL (FIXED 3 PER MODULE) ---------- */
data class Level(
    val id: Int,
    val name: LevelType,
    val image: String,
    val chapters: List<Chapter>
)

/* ---------- CHAPTER ---------- */
/*
Shown in Chapter list screen
Top card uses:
- module info
- level name
*/
data class Chapter(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val slides: List<Slide>
)

/* ---------- SLIDE ---------- */
data class Slide(
    val id: Int,
    val title: String,
    val blocks: List<SlideBlock>
)

/* ---------- SLIDE BLOCKS (VERY IMPORTANT) ---------- */
/*
This allows:
- text
- markdown
- images
- tables
- mixed content
*/


/* ---------- ENUMS ---------- */
enum class LevelType {
    HUSTLER,
    MASTERMIND,
    UNSTOPPABLE
}


