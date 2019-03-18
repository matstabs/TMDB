package com.tmdb.ui.details.person

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tmdb.TMDbApplication
import com.tmdb.helpers.ImageHelper
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.person.PersonDetails
import com.tmdb.models.details.person.images.PersonImages
import com.tmdb.models.details.person.movie.credits.PersonMovieCredits
import com.tmdb.models.details.person.tvshow.credits.PersonTvShowCredits
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.network.ConnectionState
import com.tmdb.network.NetworkModule
import com.tmdb.network.TMDbAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PersonDetailsPresenter : MvpPresenter<PersonDetailsView>() {

    @Inject
    lateinit var service: TMDbAPI
    @Inject
    lateinit var connectionState: ConnectionState

    var compositeDisposable = CompositeDisposable()

    var imageHelper: ImageHelper? = null
    var person_id = 1

    companion object {
        private const val ERROR_TAG = "ERROR_TAG"
    }

    init {
        TMDbApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.getExtra()
        if (connectionState.hasNetwork()) {
            displayPersonDetails()
        }
    }

    private fun displayPersonDetails() {
        val person = getPersonDetails(person_id, NetworkModule.API_KEY, NetworkModule.language)
        val movieCredits = getPersonMovieCredits(person_id, NetworkModule.API_KEY, NetworkModule.language)
        val tvShowCredits = getPersonTvShowCredits(person_id, NetworkModule.API_KEY, NetworkModule.language)
        val images = getPersonImages(person_id, NetworkModule.API_KEY)
        val disposable = Observable.zip(person,
            movieCredits,
            tvShowCredits,
            images,
            Function4<PersonDetails, PersonMovieCredits, PersonTvShowCredits, PersonImages, List<Any>>
            { t1, t2, t3, t4 ->  listOf(t1, t2, t3, t4)})
            .subscribe {  list ->
                viewState.showPersonDetails(list[0] as PersonDetails,
                    list[1] as PersonMovieCredits,
                    list[2] as PersonTvShowCredits,
                    list[3] as PersonImages)
            }
        compositeDisposable.add(disposable)
    }

    private fun getPersonDetails(person_id: Int,
                                 apiKey: String,
                                 language: String) = service
        .getPersonDetails(person_id, apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }
        .onErrorReturn { null }

    private fun getPersonMovieCredits(person_id: Int,
                                      apiKey: String,
                                      language: String) = service
        .getPersonMovieCredits(person_id, apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }
        .onErrorReturn { null }

    private fun getPersonTvShowCredits(person_id: Int,
                                       apiKey: String,
                                       language: String) = service
        .getPersonTvShowCredits(person_id, apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }
        .onErrorReturn { null }

    private fun getPersonImages(person_id: Int,
                                apiKey: String) = service
        .getPersonImages(person_id, apiKey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }
        .onErrorReturn { null }
}