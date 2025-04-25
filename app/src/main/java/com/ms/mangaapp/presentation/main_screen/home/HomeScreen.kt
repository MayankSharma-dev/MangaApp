package com.ms.mangaapp.presentation.main_screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.ms.mangaapp.presentation.common.SnackBarController
import com.ms.mangaapp.presentation.common.SnackbarEvent
import com.ms.mangaapp.connectivity.ConnectivityObserver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    lazyGridState: LazyGridState,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit
) {

    //val viewModel = hiltViewModel<HomeViewModel>()
    val mangas = viewModel.mangaPagingFlow.collectAsLazyPagingItems()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()

    var previousStatus by remember { mutableStateOf(networkStatus) }

    LaunchedEffect(networkStatus) {
        if (networkStatus == ConnectivityObserver.Status.Available &&
            previousStatus != ConnectivityObserver.Status.Available
        ) {
            SnackBarController.sendEvent(SnackbarEvent("Network changed."))
            mangas.refresh()
        }
        previousStatus = networkStatus
    }


    LaunchedEffect(mangas.loadState) {
        if (mangas.loadState.refresh is LoadState.Error) {
            SnackBarController.sendEvent(SnackbarEvent("Error: "+ (mangas.loadState.refresh as LoadState.Error).error.message))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (mangas.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyGridState
            ) {
                items(count = mangas.itemCount, key = mangas.itemKey { it.id }) { index ->
                    val item = mangas[index]
                    if (item != null)
                        Card(modifier = Modifier
                            .aspectRatio(0.75f)
                            .fillMaxWidth()
                            .clickable {onNavigateToDetail(item.identifier)}) {
                            AsyncImage(
                                model = item.imgUrl,
                                contentDescription = item.title,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                }
                item {
                    if (mangas.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}



