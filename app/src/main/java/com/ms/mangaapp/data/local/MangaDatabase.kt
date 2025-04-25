package com.ms.mangaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [MangaEntity::class, RemoteKeyEntity::class],
    version = 1
)
@TypeConverters(Convertors::class)
abstract class MangaDatabase : RoomDatabase() {

    abstract val mangaDao: MangaDao
    abstract val remoteKeyDao: RemoteKeyDao
}