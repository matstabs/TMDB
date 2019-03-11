package com.tmdb.models.details.tvshow.videos

import com.google.gson.annotations.SerializedName

data class TvShowVideos(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<Result>?
)