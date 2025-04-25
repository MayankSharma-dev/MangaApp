package com.ms.mangaapp.data.remote

/*
import com.google.gson.Gson
import com.ms.mangaapp.data.mappers.toMangaEntity
import com.ms.mangaapp.domain.Manga
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class MangaRepositoryImpl(
    private val client: OkHttpClient,
    private val gson: Gson
) {

    suspend fun fetchManga(page: Int): MangaResponseDTO = withContext(Dispatchers.IO){
        val request = Request.Builder()
            .url("https://mangaverse-api.p.rapidapi.com/manga/fetch?page=$page")
            .get()
            .addHeader("x-rapidapi-key", "504641jhjvliyiyf6a5f3asfaf2aa3f8a2as1a389as7dasda")
            .addHeader("x-rapidapi-host", "mangaverse-api.p.rapidapi.com")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val body = response.body?.string() ?: throw IOException("Empty body")
            val mangaResponse = gson.fromJson(body, MangaResponseDTO::class.java)
            mangaResponse
        }
    }
}
*/