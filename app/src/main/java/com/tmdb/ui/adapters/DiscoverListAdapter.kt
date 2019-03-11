package com.tmdb.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tmdb.R
import com.tmdb.helpers.FormatHelper
import com.tmdb.helpers.ImageHelper
import com.tmdb.models.discover.movie.MovieItem
import com.tmdb.models.discover.tvshow.TvShowItem
import com.tmdb.models.genres.Genre
import com.tmdb.ui.adapters.holders.BaseHolder
import com.tmdb.ui.adapters.holders.Tie
import kotlinx.android.synthetic.main.discover_list_item.view.*

class DiscoverListAdapter<T> (val data: ArrayList<T>?,
                              private val imageHelper: ImageHelper,
                              private val genres: List<Genre>?) :
    RecyclerView.Adapter<BaseHolder<T>>(), Tie<T> {

    private var clickListener: ((T) -> Unit)? = null
    private var longClickListener: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.discover_list_item, parent, false)
        return BaseHolder(view, this)
    }

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        val element = data!![position]

        when (element) {
            is TvShowItem -> {
                val el = element as TvShowItem
                bind(holder, el.posterPath, el.genreIds, el.name, el.voteAverage)
            }
            is MovieItem -> {
                val el = element as MovieItem
                bind(holder, el.posterPath, el.genreIds, el.title, el.voteAverage)
            }
        }
    }

    private fun bind(holder: BaseHolder<T>,
                     posterPath: String?,
                     genreIds: List<Int>,
                     title: String,
                     voteAverage: Double) = with(holder.view) {
        Glide.with(context)
            .load(imageHelper.getPosterUrl(posterPath))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_poster)
            .into(list_item_poster_view)
        Glide.with(context)
            .load(R.drawable.tmdb_score_logo)
            .into(list_item_score_logo)
        list_item_genres_text.text = FormatHelper.genresIdListToString(genreIds, genres)
        list_item_title_text.text = title
        list_item_score_text.text = voteAverage.toString()
    }


    override fun itemClickListener(element: T) {
        if (clickListener != null)
            clickListener?.invoke(element)
    }

    override fun longItemClickListener(element: T): Boolean {
        if (longClickListener != null)
            longClickListener?.invoke(element)
        return true
    }

    override fun getItem(position: Int): T = data!![position]

    fun setItemClickListener(action: (T) -> Unit) {
        clickListener = action
    }

    fun setLongItemClickListener(action: (T) -> Unit) {
        longClickListener = action
    }

}