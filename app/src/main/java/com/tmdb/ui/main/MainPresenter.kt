package com.tmdb.ui.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tmdb.TMDbApplication
import com.tmdb.models.configuration.Configuration
import com.tmdb.models.details.movie.SpokenLanguage
import com.tmdb.models.genres.GenresList
import com.tmdb.network.ConnectionState
import com.tmdb.network.NetworkModule
import com.tmdb.network.TMDbAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject



@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    companion object {
        private val ERROR_TAG = "main_presenter_error"
    }

    @Inject
    lateinit var service: TMDbAPI

    @Inject
    lateinit var connectionState: ConnectionState

    var compositeDisposable = CompositeDisposable()

    private var tvShowGenres: GenresList? = null
    private var movieGenres: GenresList? = null
    private var configuration: Configuration? = null

    init {
        TMDbApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        with(viewState) {
            if (connectionState.hasNetwork()) {
                val disposalble = getGenresAndConfig()
                    .subscribe { list ->
                        tvShowGenres = list[0] as GenresList
                        movieGenres = list[1] as GenresList
                        configuration = list[2] as Configuration

                        initDiscoverViewPager(
                            configuration!!.imageConfiguration,
                            tvShowGenres!!.genres,
                            movieGenres!!.genres
                        )
                        showViewPager()
                    }
                compositeDisposable.add(disposalble)
            }
        }
    }

    private fun getTvShowsGenres(apiKey: String,
                                 language: String) = service.getTvShowGenres(apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturn { null }
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }

    private fun getMovieGenres(apiKey: String,
                               language: String) = service.getMovieGenres(apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturn { null }
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }

    private fun getGetConfiguration(apiKey: String) = service.getConfiguration(apiKey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturn { null }
        .doOnError { e -> Log.e(ERROR_TAG, e.message) }


    private fun getGenresAndConfig()= Observable
                .zip(getTvShowsGenres(NetworkModule.API_KEY, NetworkModule.language) ,
                        getMovieGenres(NetworkModule.API_KEY, NetworkModule.language),
                        getGetConfiguration(NetworkModule.API_KEY),
                        Function3<GenresList, GenresList, Configuration, List<Any>>
                        { tvGenres, movieGenres, config ->  listOf(tvGenres, movieGenres, config)})

}