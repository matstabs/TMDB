package com.tmdb.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tmdb.R
import com.tmdb.helpers.ImageHelper
import com.tmdb.models.discover.movie.MovieItem
import com.tmdb.models.discover.tvshow.TvShowItem
import com.tmdb.models.videos.Trailer
import com.tmdb.ui.adapters.holders.BaseHolder
import com.tmdb.ui.adapters.holders.Tie
import kotlinx.android.synthetic.main.discover_list_item.view.*
import kotlinx.android.synthetic.main.trailers_list_item.view.*

class TrailersAdapter(private val trailers: List<Trailer>,
                      private val imageHelper: ImageHelper) : RecyclerView.Adapter< BaseHolder<Trailer> >(), Tie<Trailer> {


    private var clickListener: ((Trailer) -> Unit)? = null
    private var longClickListener: ((Trailer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Trailer> {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.trailers_list_item, parent, false)
        return BaseHolder(view, this)
    }

    override fun getItemCount() = trailers.size

    override fun onBindViewHolder(holder: BaseHolder<Trailer>, position: Int) = with(holder.view) {
        val trailer = trailers[position]
        Glide.with(context)
            .load(imageHelper.getYoutubeThumbnailUrl(trailer.key))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.random_thumbnail)
            .into(trailer_thumbnail)
        Glide.with(context)
                .load(R.drawable.youtube_play_icon)
                .into(trailer_play_icon)
        trailer_name_text.text = trailer.name
    }

    override fun itemClickListener(trailer: Trailer) {
        if (clickListener != null)
            clickListener?.invoke(trailer)
    }

    override fun longItemClickListener(trailer: Trailer): Boolean {
        if (longClickListener != null)
            longClickListener?.invoke(trailer)
        return true
    }

    override fun getItem(position: Int): Trailer = trailers[position]

    fun setItemClickListener(action: (Trailer) -> Unit) {
        clickListener = action
    }

    fun setLongItemClickListener(action: (Trailer) -> Unit) {
        longClickListener = action
    }

}