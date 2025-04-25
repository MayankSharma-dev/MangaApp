package com.ms.mangaapp.presentation.main_screen.home

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ms.mangaapp.data.local.MangaEntity
import com.ms.mangaapp.data.mappers.toManga
import com.ms.mangaapp.domain.Manga
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MangaRepository @Inject constructor(
//    private val api: MangaAPI,
//    private val database: MangaDatabase,
    private val pager: Pager<Int, MangaEntity>,
) {
    /*
    private val pagingSourceFactory = { database.mangaDao.pagingSource() }

    @OptIn(ExperimentalPagingApi::class)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 24,
            enablePlaceholders = false,
            initialLoadSize = 24
        ),
        remoteMediator = MangaRemoteMediator(
            mangaAPI = api,
            mangaDb = database,
        ),
        pagingSourceFactory = pagingSourceFactory
    )

     */

    fun getManga(): Flow<PagingData<Manga>> {
        return pager.flow.map { pagingData ->
            pagingData.map { mangaEntity ->
                mangaEntity.toManga()
            }
        }
    }

}