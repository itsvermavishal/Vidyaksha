package com.example.vidyaksha.data.local

import androidx.room.TypeConverter

/**
 * Converts a list of colors (Int) to/from a String for Room persistence.
 */
class ColorListConverter {

    @TypeConverter
    fun fromColorList(colorList: List<Int>): String {
        return colorList.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun toColorList(colorListString: String): List<Int> {
        return colorListString.split(",").map { it.toInt() }
    }
}
