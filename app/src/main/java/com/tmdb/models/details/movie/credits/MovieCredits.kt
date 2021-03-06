package com.tmdb.models.details.movie.credits

import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)