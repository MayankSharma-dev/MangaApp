package com.ms.mangaapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MangaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val identifier: String,
    val title: String,
    val subTitle: String,
    val imgUrl: String,
    val summary: String,
    val author: List<String>?,
    val type: String,
)
