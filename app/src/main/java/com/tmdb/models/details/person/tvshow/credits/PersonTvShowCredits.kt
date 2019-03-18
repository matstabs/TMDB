package com.tmdb.models.details.person.tvshow.credits

import com.google.gson.annotations.SerializedName

data class PersonTvShowCredits(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int
)