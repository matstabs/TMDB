package com.tmdb.models.videos

import com.google.gson.annotations.SerializedName

data class Trailers(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<Trailer>
)