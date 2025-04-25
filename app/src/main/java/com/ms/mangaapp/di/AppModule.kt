package com.ms.mangaapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.ms.mangaapp.BuildConfig

import com.ms.mangaapp.data.preferences.UserPreferences
import com.ms.mangaapp.data.preferences.UserPreferencesRepository
import com.ms.mangaapp.data.preferences.UserPreferencesRepository.Companion.USER_DATASTORE_FILE_NAME
import com.ms.mangaapp.data.preferences.UserPreferencesSerializer
import com.ms.mangaapp.connectivity.NetworkConnectivityObserver
import com.ms.mangaapp.data.local.MangaDatabase
import com.ms.mangaapp.data.local.MangaEntity
import com.ms.mangaapp.data.remote.MangaAPI
import com.ms.mangaapp.data.remote.MangaRemoteMediator
import com.ms.mangaapp.presentation.sign_in.AuthRepository
import com.ms.mangaapp.presentation.sign_in.GoogleAuthUiClient
import com.ms.mangaapp.presentation.main_screen.home.MangaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesGoogleAuthUiClient(app: Application) =
        GoogleAuthUiClient(app, Identity.getSignInClient(app))

    @Provides
    @Singleton
    fun providesAuthRepository(googleAuthUiClient: GoogleAuthUiClient) =
        AuthRepository(googleAuthUiClient)

    @Provides
    @Singleton
    fun providesNetworkConnectivityObserver(@ApplicationContext context: Context) =
        NetworkConnectivityObserver(context)

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(@ApplicationContext context: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile(USER_DATASTORE_FILE_NAME) },
            //scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<UserPreferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    @Singleton
    fun providesMangaDatabase(@ApplicationContext context: Context): MangaDatabase {
        return Room.databaseBuilder(
            context,
            MangaDatabase::class.java,
            "manga.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMangaApi(): MangaAPI {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", BuildConfig.API_TOKEN)
                    .addHeader("x-rapidapi-host", "mangaverse-api.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(MangaAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providesMangaPager(mangaDb: MangaDatabase, mangaAPI: MangaAPI): Pager<Int, MangaEntity> {
        return Pager(
            config = PagingConfig(pageSize = 24),
            remoteMediator = MangaRemoteMediator(
                mangaDb,
                mangaAPI
            ),
            pagingSourceFactory = {
                mangaDb.mangaDao.pagingSource()
            }
        )
    }


    @Provides
    @Singleton
    fun providesMangaRepository(pager: Pager<Int, MangaEntity>): MangaRepository {
        //return MangaRepository(mangaAPI,mangaDb)
        return MangaRepository(pager = pager)
    }

    //    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder().build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideGson(): Gson {
//        return GsonBuilder().create()
//    }
//
//    @Provides
//    @Singleton
//    fun provideMangaRepository(
//        client: OkHttpClient,
//        gson: Gson
//    ): MangaRepositoryImpl {
//        return MangaRepositoryImpl(client, gson)
//    }
}