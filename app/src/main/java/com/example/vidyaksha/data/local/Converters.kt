package com.example.vidyaksha.data.local

import androidx.room.TypeConverter

/**
 * Converts List<String> <-> String for media URIs storage in Room.
 */
class Converters {

    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
    }
}
