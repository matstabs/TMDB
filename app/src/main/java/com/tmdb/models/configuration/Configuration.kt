package com.tmdb.models.configuration

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Configuration (
    @SerializedName("change_keys")
    val changeKeys: List<String>,
    @SerializedName("images")
    val imageConfiguration: ImageConfiguration
) : PaperParcelable {

    companion object  {
        @JvmField val CREATOR = PaperParcelConfiguration.CREATOR
    }
}