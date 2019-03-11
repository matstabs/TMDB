package com.tmdb.models.discover.tvshow

import com.google.gson.annotations.SerializedName

data class DiscoverTvShow(
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<TvShowItem>?,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)