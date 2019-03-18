package com.tmdb.network

import com.tmdb.models.configuration.Configuration
import com.tmdb.models.details.movie.MovieDetails
import com.tmdb.models.details.movie.credits.MovieCredits
import com.tmdb.models.details.person.PersonDetails
import com.tmdb.models.details.person.images.PersonImages
import com.tmdb.models.details.person.movie.credits.PersonMovieCredits
import com.tmdb.models.details.person.tvshow.credits.PersonTvShowCredits
import com.tmdb.models.videos.Trailers
import com.tmdb.models.details.tvshow.TvShowDetails
import com.tmdb.models.details.tvshow.credits.TvShowCredits
import com.tmdb.models.discover.movie.DiscoverMovie
import com.tmdb.models.discover.tvshow.DiscoverTvShow
import com.tmdb.models.genres.GenresList
import io.reactivex.Observable
import retrofit2.http.*

interface TMDbAPI {

    @GET(value = "discover/movie")
    fun getDiscoverMovie(@Query("api_key") apiKey: String,
                         @Query("language") language: String = "en",
                         @Query("region") region: String? = null,
                         @Query("sort_by") sortBy: String? = null,
                         @Query("certification_country") certificationCountry: String? = null,
                         @Query("certification") certification: String? = null,
                         @Query("certification.lte") certificationLte: String? = null,
                         @Query("include_adult") includeAdult: Boolean? = null,
                         @Query("include_video") includeVideo: Boolean? = null,
                         @Query("page") page: Int = 1,
                         @Query("primary_release_year") primaryReleaseYear: Int? = null,
                         @Query("primary_release_date.gte") primaryReleaseDateGte: String? = null,
                         @Query("primary_release_date.lte") primaryReleaseDateLte: String? = null,
                         @Query("release_date.gte") releaseDateGte: String? = null,
                         @Query("release_date.lte") releaseDateLte: String? = null,
                         @Query("vote_count.gte") voteCountGte: Int? = null,
                         @Query("vote_count.lte") voteCountLte: Int? = null,
                         @Query("vote_average.gte") voteAverageGte: Double? = null,
                         @Query("vote_average.lte") voteAverageLte: Double? = null,
                         @Query("with_cast") withCast: String? = null,
                         @Query("with_crew") withCrew: String? = null,
                         @Query("with_companies") withCompanies: String? = null,
                         @Query("with_genres") withGenres: String? = null,
                         @Query("with_keywords") withKeywords: String? = null,
                         @Query("with_people") withPeople: String? = null,
                         @Query("year") year: Int? = null,
                         @Query("without_genres") withoutGenres: String? = null,
                         @Query("with_runtime.gte") withRuntimeGte: Int? = null,
                         @Query("with_runtime.lte") withRuntimeLte: Int? = null,
                         @Query("with_release_type") withReleaseType: Int? = null,
                         @Query("with_original_language") withOriginalLanguage: String? = null,
                         @Query("without_keywords") withoutKeywords: String? = null): Observable<DiscoverMovie>

    @GET(value = "discover/tv")
    fun getDiscoverTvShow(@Query("api_key") apiKey: String,
                          @Query("language") language: String = "en",
                          @Query("sort_by") sortBy: String? = null,
                          @Query("air_date.gte") airDateGte: String? = null,
                          @Query("air_date.lte") airDateLte: String? = null,
                          @Query("first_air_date.gte") firstAirDateGte: String? = null,
                          @Query("first_air_date.lte") firstAirDateLte: String? = null,
                          @Query("first_air_date_year") firstAirDateYear: Int? = null,
                          @Query("page") page: Int = 1,
                          @Query("timezone") timezone: String? = null,
                          @Query("vote_average.gte") voteAverageGte: Double? = null,
                          @Query("vote_count.gte") voteCountGte: Int? = null,
                          @Query("with_genres") withGenres: String? = null,
                          @Query("with_networks") withNetworks: String? = null,
                          @Query("without_genres") withoutGenres: String? = null,
                          @Query("with_runtime.gte") withRuntimeGte: Int? = null,
                          @Query("with_runtime.lte") withRuntimeLte: Int? = null,
                          @Query("include_null_first_air_dates") includeNullFirstAirDates: Boolean? = null,
                          @Query("with_original_language") withOriginalLanguage: String? = null,
                          @Query("without_keywords") withoutKeywords: String? = null,
                          @Query("screened_theatrically") screenedTheatrically: Boolean? = null,
                          @Query("with_companies") withCompanies: String? = null): Observable<DiscoverTvShow>

    @GET(value = "movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int,
                        @Query("api_key") apiKey: String,
                        @Query("language") language: String = "en",
                        @Query("append_to_response") appendToResponse: String? = null): Observable<MovieDetails>

    @GET(value = "movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") movieId: Int,
                        @Query("api_key") apiKey: String): Observable<MovieCredits>

    @GET(value = "movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") movieId: Int,
                       @Query("api_key") apiKey: String,
                       @Query("language") language: String = "en"): Observable<Trailers>

    @GET(value = "tv/{tv_id}")
    fun getTvShowDetails(@Path("tv_id") tvId: Int,
                         @Query("api_key") apiKey: String,
                         @Query("language") language: String = "en",
                         @Query("append_to_response") appendToResponse: String? = null): Observable<TvShowDetails>

    @GET(value = "tv/{tv_id}/credits")
    fun getTvShowCredits(@Path("tv_id") tvId: Int,
                         @Query("api_key") apiKey: String,
                         @Query("language") language: String = "en"): Observable<TvShowCredits>

    @GET(value = "tv/{tv_id}/videos")
    fun getTvShowVideos(@Path("tv_id") tvId: Int,
                        @Query("api_key") apiKey: String,
                        @Query("language") language: String = "en"): Observable<Trailers>

    @Headers("Cache-Control: public, max-stale=604800") // 1 week
    @GET(value = "configuration")
    fun getConfiguration(@Query("api_key") apiKey: String): Observable<Configuration>

    @Headers("Cache-Control: public, max-stale=604800")
    @GET(value = "genre/tv/list")
    fun getTvShowGenres(@Query("api_key") apiKey: String,
                        @Query("language") language: String = "en"): Observable<GenresList>

    @Headers("Cache-Control: public, max-stale=604800")
    @GET(value = "genre/movie/list")
    fun getMovieGenres(@Query("api_key") apiKey: String,
                        @Query("language") language: String = "en"): Observable<GenresList>

    @GET(value = "person/{person_id}")
    fun getPersonDetails(@Path("person_id") personId: Int,
                       @Query("api_key") apiKey: String,
                       @Query("language") language: String = "en"): Observable<PersonDetails>

    @GET(value = "person/{person_id}/movie_credits")
    fun getPersonMovieCredits(@Path("person_id") personId: Int,
                              @Query("api_key") apiKey: String,
                              @Query("language") language: String = "en"): Observable<PersonMovieCredits>

    @GET(value = "person/{person_id}/tv_credits")
    fun getPersonTvShowCredits(@Path("person_id") personId: Int,
                               @Query("api_key") apiKey: String,
                               @Query("language") language: String = "en"): Observable<PersonTvShowCredits>

    @GET(value = "person/{person_id}/images")
    fun getPersonImages(@Path("person_id") personId: Int,
                        @Query("api_key") apiKey: String): Observable<PersonImages>

}