package com.tmdb.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val TIMEOUT_IN_MS = 30000L
        private const val cacheSize = 10 * 1024 * 1024L // 10 MB

        const val YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v="
        const val API_KEY = "88953b680ef902c996f132a79f0527fb"
        var language = Locale.getDefault().language
    }

    @Provides
    @Named("API_KEY")
    fun provideApiKey() = API_KEY

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideCache(context: Context) = Cache(context.cacheDir, cacheSize)

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient
            .Builder()
            .cache(cache)
            .connectTimeout(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideTMDbAPI(gson: Gson,
                       rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                       okHttpClient: OkHttpClient): TMDbAPI {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
            .create(TMDbAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectionState(context: Context) = ConnectionState(context)

}