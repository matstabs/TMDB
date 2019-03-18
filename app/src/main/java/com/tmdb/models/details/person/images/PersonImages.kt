package com.tmdb.models.details.person.images

import com.google.gson.annotations.SerializedName

data class PersonImages(
    @SerializedName("id")
    val id: Int,
    @SerializedName("profiles")
    val profiles: List<Profile>
)