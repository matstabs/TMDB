package com.tmdb.ui.details.content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.tmdb.R
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tmdb.helpers.FormatHelper
import com.tmdb.helpers.ImageHelper
import com.tmdb.helpers.ObjectTypes
import com.tmdb.models.details.movie.MovieDetails
import com.tmdb.models.details.movie.credits.Cast as MovieCast
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.tvshow.TvShowDetails
import com.tmdb.models.details.tvshow.credits.Cast as TvShowCast
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.models.videos.Trailers
import com.tmdb.network.NetworkModule
import com.tmdb.ui.adapters.DetailsCastAdapter
import com.tmdb.ui.adapters.TrailersAdapter
import com.tmdb.ui.details.person.PersonDetailsActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.details_facts.*


class DetailsActivity : MvpAppCompatActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    companion object {
        const val IMAGE_CONFIGURATION_EXTRA = "configuration"
        const val GENRES_EXTRA = "genres"
        const val OBJECT_ID_EXTRA = "movie_id"
        const val OBJECT_TYPE_EXTRA = "object_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        details_reload.setOnClickListener {
            Log.d("RELOAD", "fds")
            presenter.reload()
        }

    }

    override fun getExtra() {
        presenter.objectId = intent.getIntExtra(OBJECT_ID_EXTRA, 1)
        presenter.imageHelper = ImageHelper(intent.getParcelableExtra(IMAGE_CONFIGURATION_EXTRA))
        presenter.genres = intent.getParcelableArrayListExtra(GENRES_EXTRA)
        presenter.objectType = intent.getStringExtra(OBJECT_TYPE_EXTRA)
    }

    override fun showMovieDetails(movie: MovieDetails, credits: MovieCredits, trailers: Trailers) {
        showProgressbar()
        displayTitle(movie.title, movie.releaseDate, movie.runtime, movie.genres)
        displayImages(movie.backdropPath, movie.posterPath)
        displayDescription(movie.overview)
        displayMovieFacts(movie, credits)
        displayTrailers(trailers)
        displayCast(credits.cast)
        hideProgressbar()
        showView()
    }

    override fun showTvShowDetails(tvShow: TvShowDetails, credits: TvShowCredits, trailers: Trailers) {
        showProgressbar()
        displayTitle(tvShow.name, tvShow.firstAirDate, tvShow.episodeRunTime[0], tvShow.genres)
        displayImages(tvShow.backdropPath, tvShow.posterPath)
        displayDescription(tvShow.overview)
        displayTvShowFacts(tvShow)
        displayTrailers(trailers)
        displayCast(credits.cast)
        hideProgressbar()
        showView()
    }

    private fun <T>displayTitle(title: String, date: String, runtime: Int, genres: List<T>?) {

        setSupportActionBar(details_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        details_toolbar.title = " "
        details_subtitle.text = title
        details_subtitle_time.text = FormatHelper.getDetailsTimeSubtitle(date,
                runtime, resources.getString(R.string.hrs), resources.getString(R.string.min))
        details_subtitle_genres.text = FormatHelper.genreListToString(genres)

        details_app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                scrollRange = appBarLayout.totalScrollRange

                if (scrollRange + verticalOffset == 0) {
                    details_collapsing_layout.title = title
                    isShow = true
                } else if (isShow) {
                    details_collapsing_layout.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun displayDescription(description: String?) {
        if (description != null && description.isNotEmpty())
            details_description.text = description
        else {
            details_description.visibility = View.GONE
            details_overview_subtitle.visibility = View.GONE
        }
    }

    private fun displayImages(backDrop: String?, poster: String?) {
        Glide.with(applicationContext)
            .load(presenter.imageHelper!!.getPosterUrl(poster))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_poster)
            .into(details_poster)
        Glide.with(applicationContext)
            .load(presenter.imageHelper!!.getBackdropUrl(backDrop))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_poster)
            .into(details_backdrop)
    }

    private fun displayMovieFacts(movie: MovieDetails, credits: MovieCredits) {
        original_title_fact.visibility = View.VISIBLE
        original_title_text.visibility = View.VISIBLE
        original_title_text.text = movie.originalTitle

        original_language_fact.visibility = View.VISIBLE
        original_language_text.visibility = View.VISIBLE
        original_language_text.text = movie.originalLanguage

        director_fact.visibility = View.VISIBLE
        director_text.visibility = View.VISIBLE
        director_text.text = FormatHelper.getMovieDirectorString(credits)

        budget_fact.visibility = View.VISIBLE
        budget_text.visibility = View.VISIBLE
        budget_text.text = "$${movie.budget.toString()}"

        box_office_fact.visibility = View.VISIBLE
        box_office_text.visibility = View.VISIBLE
        box_office_text.text = "$${movie.revenue.toString()}"

        production_companies_fact.visibility = View.VISIBLE
        production_companies_text.visibility = View.VISIBLE
        production_companies_text.text =
                FormatHelper.getMovieProductionCompaniesString(movie.productionCompanies)
    }

    private fun displayTvShowFacts(tvShow: TvShowDetails) {
        original_title_fact.visibility = View.VISIBLE
        original_title_text.visibility = View.VISIBLE
        original_title_text.text = tvShow.originalName

        original_language_fact.visibility = View.VISIBLE
        original_language_text.visibility = View.VISIBLE
        original_language_text.text = tvShow.originalLanguage

        show_type_fact.visibility = View.VISIBLE
        show_type_text.visibility = View.VISIBLE
        show_type_text.text = tvShow.type

        status_fact.visibility = View.VISIBLE
        status_fact_text.visibility = View.VISIBLE
        status_fact_text.text = tvShow.status

        first_episode_fact.visibility = View.VISIBLE
        first_episode_text.visibility = View.VISIBLE
        first_episode_text.text = tvShow.firstAirDate

        last_episode_fact.visibility = View.VISIBLE
        last_episode_text.visibility = View.VISIBLE
        last_episode_text.text = tvShow.lastEpisodeToAir.airDate

        networks_fact.visibility = View.VISIBLE
        networks_text.visibility = View.VISIBLE
        networks_text.text = FormatHelper.networksToString(tvShow.networks)

        created_by_fact.visibility = View.VISIBLE
        created_by_text.visibility = View.VISIBLE
        created_by_text.text = FormatHelper.createdByToString(tvShow.createdBy)


    }

    private fun displayTrailers(trailers: Trailers) {
        if (trailers.results.isNotEmpty()) {
            val adapter = TrailersAdapter(trailers.results, presenter.imageHelper!!)
            details_trailers_list.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter.setItemClickListener { trailer ->
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(NetworkModule.YOUTUBE_VIDEO_BASE_URL + trailer.key)))
            }
            details_trailers_list.adapter = adapter
        } else {
            details_trailers_list.visibility = View.GONE
            details_trailers_subtitle.visibility = View.GONE
        }
    }

    private fun <T>displayCast(cast: List<T>) {
        if (cast.isNotEmpty()) {
            val adapter = DetailsCastAdapter<T>(cast, presenter.imageHelper!!)
            details_cast_list.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter.setItemClickListener { person -> startPersonDetailsActivity(person) }
            details_cast_list.adapter = adapter
        } else {
            details_cast_list.visibility = View.GONE
            details_cast_subtitle.visibility = View.GONE
        }
    }

    private fun <T>startPersonDetailsActivity(person: T) {
        val intent  = Intent(applicationContext, PersonDetailsActivity::class.java)
        when(presenter.objectType) {
            ObjectTypes.MOVIE.objectType  -> {
                intent.putExtra(PersonDetailsActivity.PERSON_ID_EXTRA, (person as MovieCast).id)
            }
            ObjectTypes.TV_SHOW.objectType -> {
                intent.putExtra(PersonDetailsActivity.PERSON_ID_EXTRA, (person as TvShowCast).id)
            }
        }
        intent.putExtra(PersonDetailsActivity.IMAGE_CONFIGURATION_EXTRA, presenter.imageHelper!!.imageConfiguration)
        startActivity(intent)

    }

    private fun showView() {
        details_scrollview.visibility = View.VISIBLE
        details_collapsing_layout.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        details_progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        details_progressbar.visibility = View.GONE
    }

    override fun showReloadBtn() {
        details_reload.visibility = View.VISIBLE
    }

    override fun hideReloadBtn() {
        details_reload.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.compositeDisposable.clear()
    }

}