package com.tmdb.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tmdb.R
import com.tmdb.helpers.ImageHelper
import com.tmdb.ui.adapters.holders.BaseHolder
import com.tmdb.ui.adapters.holders.Tie
import kotlinx.android.synthetic.main.cast_details_list_item.view.*
import kotlinx.android.synthetic.main.discover_list_item.view.*
import com.tmdb.models.details.tvshow.credits.Cast as TvShowCast
import com.tmdb.models.details.movie.credits.Cast as MovieCast

class DetailsCastAdapter <T> (val data: List<T>?,
                              private val imageHelper: ImageHelper) :
        RecyclerView.Adapter<BaseHolder<T>>(), Tie<T> {

    private var clickListener: ((T) -> Unit)? = null
    private var longClickListener: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cast_details_list_item, parent, false)
        return BaseHolder(view, this)
    }

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        val element = data!![position]

        when (element) {
            is TvShowCast -> {
                val el = element as TvShowCast
                bind(holder, imageHelper.getProfileUrl(el.profilePath), el.name)
            }
            is MovieCast -> {
                val el = element as MovieCast
                bind(holder, imageHelper.getProfileUrl(el.profilePath), el.name)
            }
        }
    }

    private fun bind(holder: BaseHolder<T>, imagePath: String, name: String) = with(holder.view) {
        Glide.with(context)
                .load(imagePath)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.random_face)
                .into(details_cast_image)
        cast_name_text.text = name
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