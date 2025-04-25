package com.ms.mangaapp.domain

data class Manga(
    val id: Int,
    val identifier: String,
    val title: String,
    val subTitle: String,
    val imgUrl: String,
    val summary: String,
    val author: List<String>?,
    val type: String,
)