package com.tmdb.ui.main.fragments.discover

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.tmdb.R
import com.tmdb.helpers.ImageHelper
import com.tmdb.helpers.ObjectTypes
import com.tmdb.models.configuration.ImageConfiguration
import com.tmdb.models.discover.movie.MovieItem
import com.tmdb.models.discover.tvshow.TvShowItem
import com.tmdb.models.genres.Genre
import com.tmdb.ui.adapters.DiscoverListAdapter
import com.tmdb.ui.details.DetailsActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.discover_fragment.*

class DiscoverFragment : MvpAppCompatFragment(), DiscoverView, SwipeRefreshLayout.OnRefreshListener {

    companion object {

        private const val IMAGE_CONFIGURATION_ARG = "configuration"
        private const val GENRES_ARG = "genres"
        private const val OBJECT_TYPE_ARG = "object_type"


        fun getMovieDiscoverFragment(imageConfiguration: ImageConfiguration,
                                     genres: ArrayList<Genre>): DiscoverFragment {
            val fragment = DiscoverFragment()
            fragment.arguments = getArguments(imageConfiguration, genres, ObjectTypes.MOVIE.objectType)
            return fragment
        }

        fun getTvShowDiscoverFragment(imageConfiguration: ImageConfiguration,
                                      genres: ArrayList<Genre>): DiscoverFragment {
            val fragment = DiscoverFragment()
            fragment.arguments = getArguments(imageConfiguration, genres, ObjectTypes.TV_SHOW.objectType)
            return fragment
        }

        private fun getArguments(imageConfiguration: ImageConfiguration,
                                 genres: ArrayList<Genre>,
                                 objectType: String): Bundle {
            val args = Bundle()
            args.putParcelable(IMAGE_CONFIGURATION_ARG, imageConfiguration)
            args.putParcelableArrayList(GENRES_ARG, genres)
            args.putString(OBJECT_TYPE_ARG, objectType)
            return args
        }
    }

    @InjectPresenter
    lateinit var presenter: DiscoverPresenter

    var movieAdapter: DiscoverListAdapter<MovieItem>? = null
    var tvShowAdapter: DiscoverListAdapter<TvShowItem>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        discover_refresh_layout.setOnRefreshListener(this)
        initRecyclerView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    override fun getArgs() {
        if (arguments != null) {
            presenter.imageConfiguration = arguments!!.getParcelable(IMAGE_CONFIGURATION_ARG)
            presenter.genres = arguments!!.getParcelableArrayList(GENRES_ARG)
            presenter.objectType = arguments!!.getString(OBJECT_TYPE_ARG)!!
        }
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(activity)
        discover_content_list.layoutManager = manager
        discover_content_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = manager.itemCount
                val visibleItemCount = manager.childCount
                val firstVisibleItem = manager.findFirstVisibleItemPosition()

                if (!presenter.loading &&
                        firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    presenter.scrolled()
                }
            }
        })
    }

    override fun showMovies(movies: ArrayList<MovieItem>) {
        if (movieAdapter == null) {
            //initRecyclerView()
            movieAdapter = DiscoverListAdapter(movies,
                    ImageHelper(presenter.imageConfiguration),
                    presenter.genres)

            movieAdapter?.setItemClickListener { movie ->
                startActivity(getIntent(movie))
            }

            discover_content_list.adapter = movieAdapter

        } else {
            movieAdapter!!.data!!.addAll(movies)
            movieAdapter!!.notifyDataSetChanged()

        }
    }

    override fun showTvShows(tvShows: ArrayList<TvShowItem>) {
        if (tvShowAdapter == null) {
            //initRecyclerView()
            tvShowAdapter = DiscoverListAdapter(tvShows,
                    ImageHelper(presenter.imageConfiguration),
                    presenter.genres)

            tvShowAdapter?.setItemClickListener { movie ->
                startActivity(getIntent(movie))
            }

            discover_content_list.adapter = tvShowAdapter

        } else {
            tvShowAdapter!!.data!!.addAll(tvShows)
            tvShowAdapter!!.notifyDataSetChanged()

        }
    }

    private fun <T>getIntent(item: T): Intent {
        val intent = Intent(activity, DetailsActivity::class.java)
        when (item) {
            is MovieItem -> {
                intent.putExtra(DetailsActivity.OBJECT_ID_EXTRA, item.id)
            }
            is TvShowItem -> {
                intent.putExtra(DetailsActivity.OBJECT_ID_EXTRA, item.id)
            }
        }
        intent.putExtra(DetailsActivity.IMAGE_CONFIGURATION_EXTRA, presenter.imageConfiguration)
        intent.putExtra(DetailsActivity.GENRES_EXTRA, presenter.genres)
        intent.putExtra(DetailsActivity.OBJECT_TYPE_EXTRA, presenter.objectType)
        return intent
    }

    override fun onRefresh() {
        if (movieAdapter != null) {
            movieAdapter!!.data!!.clear()
            movieAdapter!!.notifyDataSetChanged()
        }
        if (tvShowAdapter != null) {
            tvShowAdapter!!.data!!.clear()
            tvShowAdapter!!.notifyDataSetChanged()
        }
        presenter.update()
    }

    override fun stopRefreshing() {
        discover_refresh_layout.isRefreshing = false
    }

    override fun startRefreshing() {
        discover_refresh_layout.isRefreshing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.compositeDisposable.dispose()
        presenter.compositeDisposable.clear()
        presenter.compositeDisposable = CompositeDisposable()
    }
}