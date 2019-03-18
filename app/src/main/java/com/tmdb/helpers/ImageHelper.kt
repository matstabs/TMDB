package com.tmdb.helpers

import com.tmdb.models.configuration.ImageConfiguration
import com.tmdb.models.details.person.movie.credits.PersonMovieCredits
import com.tmdb.models.details.person.tvshow.credits.PersonTvShowCredits

class ImageHelper(val imageConfiguration: ImageConfiguration?) {

    companion object {
        private val YOUTUBE_BASE_URL = "https://img.youtube.com/vi/"

        fun getBackdropPathForPersonDetails(profilePath: String?,
                                            moviesCredits: PersonMovieCredits,
                                            tvShowCredits: PersonTvShowCredits): String {

            val moviesListCast = moviesCredits.cast
            val moviesListCrew = moviesCredits.crew
            val tvShowListCast = tvShowCredits.cast
            val tvShowListCrew = tvShowCredits.crew

            for (movie in moviesListCast)
                if (movie.backdropPath != null && movie.backdropPath.isNotEmpty())
                    return movie.backdropPath

            for (tvShow in tvShowListCast)
                if (tvShow.backdropPath != null && tvShow.backdropPath.isNotEmpty())
                    return tvShow.backdropPath

            for (movie in moviesListCrew)
                if (movie.backdropPath != null && movie.backdropPath.isNotEmpty())
                    return movie.backdropPath

            for (tvShow in tvShowListCrew)
                if (tvShow.backdropPath != null && tvShow.backdropPath.isNotEmpty())
                    return tvShow.backdropPath

            if (profilePath != null)
                return profilePath
            else
                return ""
        }
    }

    val baseUrl = imageConfiguration!!.baseUrl
    private val posterSize  = imageConfiguration!!.posterSizes[4] //w500
    private val profileSize = imageConfiguration!!.profileSizes[1] //w185
    private val backdropSize = imageConfiguration!!.profileSizes[2] //w1280

    fun getPosterUrl(path: String?) = if (path != null && !path.isEmpty())
        baseUrl + posterSize + path
    else
        ""
    fun getProfileUrl(path: String?) = if (path != null && !path.isEmpty())
        baseUrl + profileSize + path
    else
        ""
    fun getBackdropUrl(path: String?) = if (path != null && !path.isEmpty())
        baseUrl + backdropSize + path
    else
        " "
    fun getYoutubeThumbnailUrl(key: String) = YOUTUBE_BASE_URL + key + "/0.jpg"

}