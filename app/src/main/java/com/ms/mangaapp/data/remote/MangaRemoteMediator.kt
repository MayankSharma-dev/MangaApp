package com.ms.mangaapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ms.mangaapp.data.local.MangaDatabase
import com.ms.mangaapp.data.local.MangaEntity
import com.ms.mangaapp.data.local.RemoteKeyEntity
import com.ms.mangaapp.data.mappers.toMangaEntity
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MangaRemoteMediator(
    private val mangaDb: MangaDatabase,
    private val mangaAPI: MangaAPI,
) : RemoteMediator<Int, MangaEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MangaEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    val remoteKey = mangaDb.remoteKeyDao.getRemoteKey()
                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val mangasResponse = mangaAPI.getManga(page = loadKey)
            val endOfPaginationReached = mangasResponse.data.isEmpty()

            mangaDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mangaDb.mangaDao.clearAll()
                    mangaDb.remoteKeyDao.clearRemoteKey()
                }

                if (!endOfPaginationReached) {
                    val nextKey = loadKey + 1
                    mangaDb.remoteKeyDao.insertOrReplace(RemoteKeyEntity(nextPage = nextKey))
                    mangaDb.mangaDao.upsertAll(mangasResponse.data.map { it.toMangaEntity() })
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}