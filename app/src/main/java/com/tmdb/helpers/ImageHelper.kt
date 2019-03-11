package com.tmdb.helpers

import com.tmdb.models.configuration.ImageConfiguration

class ImageHelper(private val imageConfiguration: ImageConfiguration?) {

    companion object {
        private val YOUTUBE_BASE_URL = "https://img.youtube.com/vi/"
    }

    val baseUrl = imageConfiguration!!.baseUrl
    private val posterSize  = imageConfiguration!!.posterSizes[4] //w500
    private val profileSize = imageConfiguration!!.profileSizes[1] //w185
    private val backdropSize = imageConfiguration!!.profileSizes[2] //w1280

    fun getPosterUrl(name: String?) = if (name != null && !name.isEmpty())
        baseUrl + posterSize + name
    else
        ""
    fun getProfileUrl(name: String?) = if (name != null && !name.isEmpty())
        baseUrl + profileSize + name
    else
        ""
    fun getBackdropUrl(name: String?) = if (name != null && !name.isEmpty())
        baseUrl + backdropSize + name
    else
        " "
    fun getYoutubeThumbnailUrl(key: String) = YOUTUBE_BASE_URL + key + "/0.jpg"

}