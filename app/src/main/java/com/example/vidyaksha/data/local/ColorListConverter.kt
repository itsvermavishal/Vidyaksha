package com.example.vidyaksha.data.local

import androidx.room.TypeConverters

class ColorListConverter {

    @TypeConverters
    fun fromColorList(colorList: List<Int>): String{
        return colorList.joinToString(","){it.toString()}
    }

    @TypeConverters
    fun toColorList(colorListString: String): List<Int>{
        return colorListString.split(",").map { it.toInt() }
    }
}