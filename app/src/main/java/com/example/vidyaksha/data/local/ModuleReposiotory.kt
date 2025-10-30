package com.example.vidyaksha.data.local

import com.example.vidyaksha.R

data class Slide(
    val id: Int,
    val title: String,
    val textContent: String,
    val imageRes: Int? = null
)

data class Chapter(
    val id: Int,
    val title: String,
    val description: String = "",
    val slides: List<Slide>
)

data class Module(
    val id: Int,
    val title: String,
    val description: String,
    val chapters: List<Chapter>
)

object ModuleRepository {
    val modules = listOf(
        Module(
            id = 1,
            title = "Hustler",
            description = "Start with this module if you are new to the topic. Learn key fundamentals, progress through levels, and earn your certificate.",
            chapters = listOf(
                Chapter(
                    id = 1,
                    title = "Stock Market Basics",
                    description = "Understand what stocks are, how they work, and how they are traded.",
                    slides = List(10) { index ->
                        Slide(
                            id = index + 1,
                            title = "Stock Market Basics - Slide ${index + 1}",
                            textContent = "This slide explains basic stock market concepts step by step.",
                            imageRes = R.drawable.bull_logo
                        )
                    }
                ),
                Chapter(
                    id = 2,
                    title = "IPO",
                    description = "Learn what an Initial Public Offering is and how companies get listed.",
                    slides = List(8) { index ->
                        Slide(
                            id = index + 1,
                            title = "IPO - Slide ${index + 1}",
                            textContent = "This slide introduces IPOs and their importance in the market.",
                            imageRes = R.drawable.bull_logo
                        )
                    }
                ),
                Chapter(
                    id = 3,
                    title = "Investing",
                    description = "Grasp key investing principles that drive financial growth.",
                    slides = List(12) { index ->
                        Slide(
                            id = index + 1,
                            title = "Investing - Slide ${index + 1}",
                            textContent = "Learn practical investing strategies in this section.",
                            imageRes = R.drawable.bull_logo
                        )
                    }
                )
            )
        )
    )
}
