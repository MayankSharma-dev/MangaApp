package com.ms.mangaapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Upsert
    suspend fun upsertAll(mangas: List<MangaEntity>)

    @Query("SELECT * FROM mangaentity")
    fun pagingSource(): PagingSource<Int,MangaEntity>

    @Query("SELECT * FROM mangaentity WHERE identifier = :id")
    fun getMangaById(id: String): Flow<MangaEntity?>

    @Query("DELETE FROM mangaentity")
    suspend fun clearAll()
}