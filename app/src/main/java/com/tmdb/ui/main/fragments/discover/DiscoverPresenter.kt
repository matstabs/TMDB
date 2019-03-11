package com.tmdb.ui.main.fragments.discover

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tmdb.TMDbApplication
import com.tmdb.helpers.ObjectTypes
import com.tmdb.models.configuration.ImageConfiguration
import com.tmdb.models.genres.Genre
import com.tmdb.network.ConnectionState
import com.tmdb.network.NetworkModule
import com.tmdb.network.TMDbAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class DiscoverPresenter : MvpPresenter< DiscoverView >() {

    @Inject
    lateinit var service: TMDbAPI
    @Inject
    lateinit var connectionState: ConnectionState

    var imageConfiguration: ImageConfiguration? = null
    var genres: ArrayList<Genre>? = ArrayList()
    var objectType = ""

    var curPage = 0
    var maxPage = 1
    var loading = false

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        private const val ERROR_TAG = "ERROR_TAG"
    }

    init {
        TMDbApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.getArgs()

        when(objectType) {
            ObjectTypes.MOVIE.objectType -> displayMovies()
            ObjectTypes.TV_SHOW.objectType -> displayTvShow()
        }

    }

    private fun displayMovies() {
        loadMovies(curPage + 1)
    }

    private fun displayTvShow() {
        loadTvShows(curPage + 1)
    }


    private fun loadMovies(page: Int) {
        if (connectionState.hasNetwork() && page <= maxPage) {
            loading = true
            viewState.startRefreshing()
            val disposable = getMovies(NetworkModule.API_KEY, NetworkModule.language, page)
                    .subscribe { movie ->
                        val movies = movie.results
                        curPage = movie.page
                        maxPage = movie.totalPages
                        viewState.stopRefreshing()
                        viewState.showMovies(ArrayList(movies))
                        loading = false
                    }
        }
    }

    private fun loadTvShows(page: Int) {
        if (connectionState.hasNetwork() && page <= maxPage) {
            loading = true
            viewState.startRefreshing()
            val disposable = getTvShows(NetworkModule.API_KEY, NetworkModule.language, page)
                    .subscribe { tvShow ->
                        val tvShows = tvShow.results
                        curPage = tvShow.page
                        maxPage = tvShow.totalPages
                        viewState.stopRefreshing()
                        viewState.showTvShows(ArrayList(tvShows))
                        loading = false

                    }
            compositeDisposable.add(disposable)
        }
    }

    fun scrolled() {
        when(objectType) {
            ObjectTypes.MOVIE.objectType -> loadMovies(curPage + 1)
            ObjectTypes.TV_SHOW.objectType -> loadTvShows(curPage + 1)
        }
    }

    fun update() {
        curPage = 0
        maxPage = 1
        loading = false

        when(objectType) {
            ObjectTypes.MOVIE.objectType -> loadMovies(curPage + 1)
            ObjectTypes.TV_SHOW.objectType -> loadTvShows(curPage + 1)
        }
    }

    fun getMovies(apiKey: String,
                  language: String,
                  page: Int) = service.getDiscoverMovie(apiKey,
                        language = language,
                        page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {e -> Log.e(ERROR_TAG, e.message)}
            .onErrorReturn { null }

    fun getTvShows(apiKey: String,
                   language: String,
                   page: Int) = service.getDiscoverTvShow(apiKey,
                        language = language,
                        page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {e -> Log.e(ERROR_TAG, e.message)}
            .onErrorReturn { null }

}