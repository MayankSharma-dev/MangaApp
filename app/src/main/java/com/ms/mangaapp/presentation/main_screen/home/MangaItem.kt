package com.ms.mangaapp.presentation.main_screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage

import com.ms.mangaapp.domain.Manga
import com.ms.mangaapp.ui.theme.MangaAppTheme

@Composable
fun MangaItem(
    manga: Manga,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(modifier = modifier
        .aspectRatio(0.75f)
        .fillMaxWidth()
        .clickable { onClick() }) {
        AsyncImage(
            model = manga.imgUrl,
            contentDescription = manga.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Preview
@Composable
fun MangaItemPreview() {
    MangaAppTheme {
        MangaItem(
            manga = Manga(
                546,
                "dcssf",
                "fewew",
                "wfewfewf",
                "wfwfwefewf",
                "fwfewfwef",
                listOf(""),
                ""
            ),
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        )
    }
}
