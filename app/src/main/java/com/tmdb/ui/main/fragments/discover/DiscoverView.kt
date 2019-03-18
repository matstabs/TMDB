package com.tmdb.ui.main.fragments.discover

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.tmdb.models.discover.movie.MovieItem
import com.tmdb.models.discover.tvshow.TvShowItem

@StateStrategyType(value = AddToEndStrategy::class)
interface DiscoverView : MvpView {

    fun showMovies(movies: ArrayList<MovieItem>)
    fun showTvShows(tvShows: ArrayList<TvShowItem>)
    fun stopRefreshing()
    fun startRefreshing()
    fun getArgs()

}