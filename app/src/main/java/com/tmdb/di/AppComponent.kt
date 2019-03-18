package com.tmdb.di

import com.tmdb.network.NetworkModule
import com.tmdb.ui.details.content.DetailsPresenter
import com.tmdb.ui.details.person.PersonDetailsPresenter
import com.tmdb.ui.main.MainActivity
import com.tmdb.ui.main.MainPresenter
import com.tmdb.ui.main.fragments.discover.DiscoverPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(discoverActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(detailsPresenter: DetailsPresenter)
    fun inject(discoverPresenter: DiscoverPresenter)
    fun inject(personDetailsPresenter: PersonDetailsPresenter)

}