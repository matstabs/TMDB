package com.tmdb.ui.details.person

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.tmdb.models.details.person.PersonDetails
import com.tmdb.models.details.person.images.PersonImages
import com.tmdb.models.details.person.movie.credits.PersonMovieCredits
import com.tmdb.models.details.person.tvshow.credits.PersonTvShowCredits

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface PersonDetailsView : MvpView {

    fun getExtra()
    fun showPersonDetails(person: PersonDetails,
                          movieCredits: PersonMovieCredits, tvShowCredits: PersonTvShowCredits, images: PersonImages)

}