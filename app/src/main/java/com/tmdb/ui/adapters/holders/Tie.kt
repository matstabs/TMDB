package com.tmdb.ui.adapters.holders

import com.tmdb.models.discover.tvshow.TvShowItem

interface Tie<T> {

    fun itemClickListener(element: T)
    fun longItemClickListener(element: T): Boolean
    fun getItem(position: Int): T

}