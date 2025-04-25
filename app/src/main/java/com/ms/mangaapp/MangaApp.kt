package com.ms.mangaapp

import android.app.Application
import coil3.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
//import org.conscrypt.Conscrypt
import java.security.Security

@HiltAndroidApp
class MangaApp: Application(){
    override fun onCreate() {
        super.onCreate()
        //Security.insertProviderAt(Conscrypt.newProvider(), 1)
        /*
        Coil.setImageLoader {
            val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
                .build()

            val okHttpClient = OkHttpClient.Builder()
                .connectionSpecs(listOf(connectionSpec, ConnectionSpec.COMPATIBLE_TLS))
                .build()

            ImageLoader.Builder(this)
                .components {
                    add(
                        OkHttpNetworkFetcherFactory(
                            callFactory = {
                                okHttpClient
                            }
                        )
                    )
                }
                .build()
        }*/
        /*
        Coil.setImageLoader {
            ImageLoader.Builder(this)
                .components {
                    add(
                        OkHttpNetworkFetcherFactory(
                            callFactory = {
                                OkHttpClient()
                            }
                        )
                    )
                }
                .build()
        }*/
    }
}