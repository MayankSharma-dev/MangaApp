package com.ms.mangaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE label = 'manga_page'")
    suspend fun getRemoteKey(): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE label = 'manga_page'")
    suspend fun clearRemoteKey()
}