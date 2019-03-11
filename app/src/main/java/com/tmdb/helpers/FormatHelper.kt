package com.tmdb.helpers

import com.tmdb.models.details.movie.ProductionCompany as MovieProductionCompany
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.tvshow.CreatedBy
import com.tmdb.models.details.tvshow.Network
import com.tmdb.models.details.tvshow.ProductionCompany as TvShowProductionCompany
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.models.genres.Genre
import com.tmdb.models.details.movie.Genre as MovieGenre
import com.tmdb.models.details.tvshow.Genre as TvShowGenre

class FormatHelper {

    companion object {

        private val DIRECTOR_JOB = "Director"

        @Suppress("UNCHECKED_CAST")
        fun <T>genreListToString(genres: List<T>?)  = if (genres != null && genres.isNotEmpty()) {
            when (genres[0]) {
                is TvShowGenre -> tvShowGenreListToString(genres as List<TvShowGenre>)
                is MovieGenre -> movieGenreListToString(genres as List<MovieGenre>)
                else -> ""
            }
        } else {
            ""
        }

        private fun tvShowGenreListToString(genres: List<TvShowGenre>): String {
            var result = ""
            var first = true
            for(genre in genres)
                if(first) {
                    result += firstLetterToUpperCase(genre.name)
                    first = false
                } else
                    result += ", " + firstLetterToUpperCase(genre.name)
            return result
        }

        private fun movieGenreListToString(genres: List<MovieGenre>): String {
            var result = ""
            var first = true
            for(genre in genres)
                if(first) {
                    result += firstLetterToUpperCase(genre.name)
                    first = false
                } else
                    result += ", " + firstLetterToUpperCase(genre.name)
            return result
        }

        fun genresIdListToString(genresIdList: List<Int>, genresNamesList: List<Genre>?): String {
            var first = true
            var result = ""
            if (genresNamesList != null)
                for (id in genresIdList)
                    for (genre in genresNamesList)
                        if (id == genre.id)
                            if (first) {
                                result += firstLetterToUpperCase(genre.name)
                                first = false
                            } else
                                result += ", " + firstLetterToUpperCase(genre.name)
            return result
        }

        private fun firstLetterToUpperCase(string: String): String {
            return string.substring(0,1).toUpperCase() +  string.substring(1)
        }

        fun getDetailsTimeSubtitle(date: String, runtime: Int, hrs: String, min: String): String {
            var result = date.substring(0, 4) + " •"
            val hours = runtime / 60
            val minutes = (runtime % 60)
            if (hours != 0)
                result += " ${hours.toString()} $hrs "
            if (minutes != 0)
                result += " ${minutes.toString()} $min "
            return result
        }

        fun getMovieDirectorString(credits: MovieCredits?): String = if (credits != null) {
            var result = ""
            var first = true
            val crew = credits.crew
            for (person in crew)
                if (person.job == DIRECTOR_JOB)
                    if (first) {
                        result += person.name
                        first = false
                    } else
                        result += ", " + person.name

            result
        } else {
            ""
        }

        fun getMovieProductionCompaniesString(companies: List<MovieProductionCompany>): String {
            var result = ""
            var first = true
            for (company in  companies) {
                if (first) {
                    result += company.name
                    first = false
                } else
                    result += ", ${company.name}"
            }
            return result
        }

        fun createdByToString(createdBy: List<CreatedBy>?)  = if (createdBy != null) {
            var result = ""
            var first = true
            for (creator in createdBy)
                if (first) {
                    first = false
                    result += creator.name
                } else
                    result += ", ${creator.name}"
            result

        } else {
            ""
        }

        fun networksToString(networks: List<Network>?) = if (networks != null) {
            var result = ""
            var first = true
            for (network in networks)
                if (first) {
                    first = false
                    result += network.name
                } else
                    result += ", ${network.name}"
            result
        } else {
            ""
        }
    }

}