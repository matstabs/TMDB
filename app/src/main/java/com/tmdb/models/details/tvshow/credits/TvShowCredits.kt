package com.tmdb.models.details.tvshow.credits

import com.google.gson.annotations.SerializedName

data class TvShowCredits(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)