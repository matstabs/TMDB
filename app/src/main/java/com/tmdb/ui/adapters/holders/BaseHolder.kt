package com.tmdb.ui.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tmdb.models.discover.movie.MovieItem

class BaseHolder<T> (val view: View, tie: Tie<T>) : RecyclerView.ViewHolder(view) {

    init {
        with(tie) {
            view.setOnClickListener{ itemClickListener(getItem(adapterPosition)) }
            view.setOnLongClickListener{ longItemClickListener(getItem(adapterPosition))}
        }
    }

}