package com.ms.mangaapp.data.mappers

import com.ms.mangaapp.data.local.MangaEntity
import com.ms.mangaapp.data.remote.MangaDTO
import com.ms.mangaapp.domain.Manga

fun MangaDTO.toMangaEntity(): MangaEntity {
    return MangaEntity(
        identifier = identifier,
        title = title,
        subTitle = subTitle,
        imgUrl = thumb,
        summary = summary,
        author = author,
        type = type
    )
}

fun MangaEntity.toManga(): Manga {
    return Manga(
        id = id,
        identifier = identifier,
        title = title,
        subTitle = subTitle,
        imgUrl = imgUrl,
        summary = summary,
        author = author,
        type = type
    )
}