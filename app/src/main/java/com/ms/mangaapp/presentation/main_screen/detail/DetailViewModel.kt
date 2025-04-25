package com.ms.mangaapp.presentation.main_screen.detail

import androidx.lifecycle.ViewModel
import com.ms.mangaapp.data.local.MangaDatabase
import com.ms.mangaapp.data.mappers.toManga
import com.ms.mangaapp.domain.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
   private val mangaDb: MangaDatabase
): ViewModel() {

    fun getMangaById(id: String): Flow<Manga?>{
       return mangaDb.mangaDao.getMangaById(id).map { it?.toManga() }
    }
}