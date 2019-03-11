package com.tmdb.models.configuration

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class ImageConfiguration(
    @SerializedName("backdrop_sizes")
    val backdropSizes: List<String>,
    @SerializedName("base_url")
    val baseUrl: String,
    @SerializedName("logo_sizes")
    val logoSizes: List<String>,
    @SerializedName("poster_sizes")
    val posterSizes: List<String>,
    @SerializedName("profile_sizes")
    val profileSizes: List<String>,
    @SerializedName("secure_base_url")
    val secureBaseUrl: String,
    @SerializedName("still_sizes")
    val stillSizes: List<String>
) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelImageConfiguration.CREATOR
    }

}