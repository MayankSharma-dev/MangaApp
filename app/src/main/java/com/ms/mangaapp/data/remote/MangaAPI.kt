package com.ms.mangaapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MangaAPI {

    @GET("manga/latest")
    suspend fun getManga(
        @Query("page") page: Int
    ): MangaResponseDTO


    companion object{
        const val BASE_URL = "https://mangaverse-api.p.rapidapi.com/"
    }
}