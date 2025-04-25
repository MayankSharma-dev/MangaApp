package com.ms.mangaapp.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.intellij.lang.annotations.Identifier

@JsonClass(generateAdapter = true)
data class MangaResponseDTO(
    val code: Int,
    @Json(name = "data") val data: List<MangaDTO>
)

@JsonClass(generateAdapter = true)
data class MangaDTO(
    @Json(name = "id") val identifier: String,
    val title: String,
    @Json(name = "sub_title")val subTitle: String,
    val thumb: String,
    val summary: String,
    val author: List<String>?,
    val type: String,
)