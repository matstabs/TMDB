package com.tmdb

import android.app.Application
import com.tmdb.di.AppComponent
import com.tmdb.di.AppModule
import com.tmdb.di.DaggerAppComponent

class TMDbApplication : Application() {

    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .build()

    }
}