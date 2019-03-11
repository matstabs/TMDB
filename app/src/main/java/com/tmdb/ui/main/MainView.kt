package com.tmdb.ui.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.tmdb.models.configuration.ImageConfiguration
import com.tmdb.models.genres.Genre

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {

    //fun initNavigationDrawer()
    fun initDiscoverViewPager(configuration: ImageConfiguration, tvShowGenres: List<Genre>, movieGenres: List<Genre>)

    fun showViewPager()
    fun hideViewPager()

}