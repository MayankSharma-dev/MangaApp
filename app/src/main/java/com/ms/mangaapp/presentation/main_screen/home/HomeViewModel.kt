package com.ms.mangaapp.presentation.main_screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ms.mangaapp.connectivity.ConnectivityObserver
import com.ms.mangaapp.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    mangaRepository: MangaRepository,
    networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val networkStatus = networkConnectivityObserver.observer()
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Available)

        val mangaPagingFlow = mangaRepository.getManga()
        .cachedIn(viewModelScope)
}