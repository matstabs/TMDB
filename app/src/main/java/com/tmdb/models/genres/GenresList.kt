package com.tmdb.models.genres

import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class GenresList(
        @SerializedName("genres")
        val genres: List<Genre>
) : PaperParcelable {

    companion object {

    }

}