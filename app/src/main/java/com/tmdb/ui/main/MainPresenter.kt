package com.tmdb.ui.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tmdb.TMDbApplication
import com.tmdb.models.configuration.Configuration
import com.tmdb.models.genres.GenresList
import com.tmdb.network.NetworkModule
import com.tmdb.network.TMDbAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.security.cert.Certificate
import java.util.*
import javax.inject.Inject



@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var service: TMDbAPI

    var disposalble: Disposable? = null

    private var tvShowGenres: GenresList? = null
    private var movieGenres: GenresList? = null
    private var configuration: Configuration? = null

    companion object {
        private const val TV_GENRES_LOADING_ERROR_TAG = "tv_genres_loading_error"
        private const val MOVIE_GENRES_LOADING_ERROR_TAG = "movie_genres_loading_er"
        private const val CONFIGURATION_LOADING_ERROR_TAG = "configuration_load_er"
    }

    init {
        TMDbApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        with(viewState) {
            //initNavigationDrawer()

            disposalble = loadGenresAndConfig()
                .subscribe {list ->
                    tvShowGenres = list[0] as GenresList
                    movieGenres = list[1] as GenresList
                    configuration = list[2] as Configuration

                    initDiscoverViewPager(configuration!!.imageConfiguration,
                        tvShowGenres!!.genres,
                        movieGenres!!.genres)
                    showViewPager()
                }
        }
    }

    override fun attachView(view: MainView?) {
        super.attachView(view)

        val tvGenres = service.getTvShowGenres(NetworkModule.API_KEY, NetworkModule.language)
        val movieGenres = service.getMovieGenres(NetworkModule.API_KEY, NetworkModule.language)
        val config = service.getConfiguration(NetworkModule.API_KEY)

        initView()

    }

    private fun initView() {}

    private fun loadGenresAndConfig(): Observable< List<Any> > {
        return Observable
                .zip(service.getTvShowGenres(NetworkModule.API_KEY, NetworkModule.language) ,
                        service.getMovieGenres(NetworkModule.API_KEY, NetworkModule.language),
                        service.getConfiguration(NetworkModule.API_KEY),
                        Function3<GenresList, GenresList, Configuration, List<Any>>
                        { tvGenres, movieGenres, config ->  listOf(tvGenres, movieGenres, config)})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}