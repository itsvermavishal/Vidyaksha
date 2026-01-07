package com.example.vidyaksha.data.local

import com.example.vidyaksha.R

object ContentMapper {

    fun imageRes(name: String): Int {
        return when (name) {
            "stockmarkets" -> R.drawable.stockmarkets
            "slide_stock_intro" -> R.drawable.stock
            // 🔹 MODULE IMAGES
            "fundametal" -> R.drawable.fundametal
            "techicals" -> R.drawable.techicals
            "personalfinances" -> R.drawable.personalfinances
            "comodity" -> R.drawable.comodity
            "futureandoptions" -> R.drawable.futureandoptions

            // 🔹 LEVEL IMAGES
            "hustler" -> R.drawable.hustler
            "mastermind" -> R.drawable.mastermind
            "unstoppable" -> R.drawable.unstoable

            // 🔹 CHAPTER IMAGES
            "stock" -> R.drawable.stock
            "balance_sheet" -> R.drawable.balance_sheet1

            // 🔹 SLIDE IMAGES
            "slide_stock_intro" -> R.drawable.stock
            "slide_stock_chart" -> R.drawable.stock

            // 🔻 fallback
            else -> {
                android.util.Log.e("ContentMapper", "Unknown image key: $name")
                R.drawable.sample
            }
        }
    }
}