package com.tmdb.models.details.person.movie.credits

import com.google.gson.annotations.SerializedName

data class PersonMovieCredits(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)