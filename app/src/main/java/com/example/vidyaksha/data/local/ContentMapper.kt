package com.example.vidyaksha.data.local

import android.util.Log
import com.example.vidyaksha.R

object ContentMapper {

    fun imageRes(name: String?): Int {
        if (name.isNullOrBlank()) {
            Log.e("ContentMapper", "❌ Image name is null or blank")
            return R.drawable.placeholder_image
        }
        val key = name
            .trim()
            .lowercase()
            .replace("-", "_")
            .replace(" ", "_")

        return when (key) {

            // 🔹 Modules
            "stockmarkets" -> R.drawable.stockmarkets
            "fundamental" -> R.drawable.fundametal
            "techicals" -> R.drawable.techicals
            "personalfinances" -> R.drawable.personalfinances
            "comodity" -> R.drawable.comodity
            "futureandoptions" -> R.drawable.futureandoptions

            // 🔹 Levels
            "hustler" -> R.drawable.hustler
            "mastermind" -> R.drawable.mastermind
            "unstoppable" -> R.drawable.unstoable

            // 🔹 Chapters / slides
            "stock" -> R.drawable.stock
            "balance_sheet" -> R.drawable.balance_sheet1
            "slide_stock_intro" -> R.drawable.stock
            "slide_stock_chart" -> R.drawable.stock
            "market_rally_1" -> R.drawable.stock
            "market_rally_2" -> R.drawable.stock

            else -> {
                Log.e("ContentMapper", "❌ Unknown image key: $name")
                R.drawable.placeholder_image
            }
        }
    }
}
