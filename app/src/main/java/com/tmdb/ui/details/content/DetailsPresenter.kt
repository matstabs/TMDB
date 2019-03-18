package com.tmdb.ui.details.content

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.tmdb.TMDbApplication
import com.tmdb.helpers.ImageHelper
import com.tmdb.helpers.ObjectTypes
import com.tmdb.models.details.movie.MovieDetails
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.tvshow.TvShowDetails
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.models.genres.Genre
import com.tmdb.models.videos.Trailers
import com.tmdb.network.ConnectionState
import com.tmdb.network.NetworkModule
import com.tmdb.network.TMDbAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject



@InjectViewState
class DetailsPresenter : MvpPresenter<DetailsView>() {

    @Inject
    lateinit var service: TMDbAPI
    @Inject
    lateinit var connectionState: ConnectionState

    var imageHelper: ImageHelper? = null
    var genres: ArrayList<Genre>? = ArrayList()
    var objectType: String = ""
    var objectId: Int = 1


    var compositeDisposable = CompositeDisposable()

    init {
        TMDbApplication.graph.inject(this)
    }

    companion object {
        private const val ERROR_TAG = "ERROR_TAG"
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.getExtra()
        if (connectionState.hasNetwork())
            displayDetails()
        else
            viewState.showReloadBtn()

    }


    private fun  displayDetails() {
        when(objectType) {
            ObjectTypes.MOVIE.objectType -> displayMovieDetails()
            ObjectTypes.TV_SHOW.objectType -> displayTvShowDetails()
            else -> Log.e(ERROR_TAG, "something wrong with types in details activity")
        }
    }

    private fun displayMovieDetails() {
        val movie = getMovieDetails(objectId, NetworkModule.API_KEY, NetworkModule.language)
        val credits = getMovieCredits(objectId, NetworkModule.API_KEY)
        val videos = getMovieVideos(objectId, NetworkModule.API_KEY, NetworkModule.language)
        val disposable = Observable.zip(movie,
            credits,
            videos,
            Function3< MovieDetails, MovieCredits, Trailers, List<Any> > { t1, t2, t3 -> listOf(t1, t2, t3) })
            .subscribe { list ->
                viewState.showMovieDetails(list[0] as MovieDetails, list[1] as MovieCredits, list[2] as Trailers)
            }
        compositeDisposable.add(disposable)
    }

    private fun displayTvShowDetails() {
        val tvShow = getTvShowDetails(objectId, NetworkModule.API_KEY, NetworkModule.language)
        val credits = getTvShowCredits(objectId, NetworkModule.API_KEY)
        val videos = getTvShowVideos(objectId, NetworkModule.API_KEY, NetworkModule.language)
        val disposable = Observable.zip(tvShow,
                credits,
                videos,
                Function3< TvShowDetails, TvShowCredits, Trailers, List<Any> > { t1, t2, t3 -> listOf(t1, t2, t3) })
                .doOnError { e -> Log.e(ERROR_TAG, e.message)  }
                .subscribe { list ->
                    viewState.showTvShowDetails(list[0] as TvShowDetails, list[1] as TvShowCredits, list[2] as Trailers)
                }
        compositeDisposable.add(disposable)
    }

    private fun getMovieDetails(movieId: Int,
                        apiKey: String,
                        language: String) = service
            .getMovieDetails(movieId, apiKey, language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {e -> Log.e(ERROR_TAG, e.message)}
            .onErrorReturn { null }

    private fun getMovieCredits(movieId: Int,
                        apiKey: String) = service
            .getMovieCredits(movieId, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {e -> Log.e(ERROR_TAG, e.message)}
            .onErrorReturn { null }


    private fun getMovieVideos(movieId: Int,
                               apiKey: String,
                               language: String) = service
        .getMovieVideos(movieId, apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {e -> Log.e(ERROR_TAG, e.message)}
        .onErrorReturn { null }

    private fun getTvShowVideos(movieId: Int,
                               apiKey: String,
                               language: String) = service
        .getTvShowVideos(movieId, apiKey, language)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {e -> Log.e(ERROR_TAG, e.message)}
        .onErrorReturn { null }

    private fun getTvShowCredits(tvShowId: Int,
                         apiKey: String) = service
        .getTvShowCredits(tvShowId, apiKey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {e -> Log.e(ERROR_TAG, e.message)}
        .onErrorReturn { null }

    private fun getTvShowDetails(tvShowId: Int,
                                 apiKey: String,
                                 language: String) = service
            .getTvShowDetails(tvShowId, apiKey, language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {e -> Log.e(ERROR_TAG, e.message)}
            .onErrorReturn { null }

    fun reload() {
        if (connectionState.hasNetwork()) {
            displayDetails()
            viewState.hideReloadBtn()
        }
    }

}