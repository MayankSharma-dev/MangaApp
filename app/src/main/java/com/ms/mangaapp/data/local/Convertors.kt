package com.ms.mangaapp.data.local

import androidx.room.TypeConverter

class Convertors {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        return string?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }
}