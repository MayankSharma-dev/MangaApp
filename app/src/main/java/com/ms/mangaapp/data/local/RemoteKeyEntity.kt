package com.ms.mangaapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val label: String = "manga_page", // A fixed label since we're tracking pages
    val nextPage: Int?
)
