package com.tmdb.models.genres

import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelGenre.CREATOR
    }
}