package com.tmdb.ui.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.tmdb.models.details.movie.MovieDetails
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.tvshow.TvShowDetails
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.models.videos.Trailers

@StateStrategyType(value = SingleStateStrategy::class)
interface DetailsView : MvpView {

    fun getExtra()
    fun showMovieDetails(movie: MovieDetails, credits: MovieCredits, trailers: Trailers)
    fun showTvShowDetails(tvShow: TvShowDetails, credits: TvShowCredits, trailers: Trailers)
    fun showReloadBtn()
    fun hideReloadBtn()
}