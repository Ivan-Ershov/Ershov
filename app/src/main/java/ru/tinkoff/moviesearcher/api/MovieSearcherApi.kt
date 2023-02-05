package ru.tinkoff.moviesearcher.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import ru.tinkoff.moviesearcher.model.MovieDetail

interface MovieSearcherApi {

    @GET("/api/v2.2/films/top")
    fun getMoviesTop(@Query("type") collection: String = "TOP_100_POPULAR_FILMS",
                     @Query("page") page: Int = 1,
                     @Header("X-API-KEY") key: String = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b",
                     @Header("Content-Type") contentType: String = "application/json")
    : Call<MoviesTopResponse>

    @GET("/api/v2.2/films/{id}")
    fun getMovie(@Path("id") id: Int,
                 @Header("X-API-KEY") key: String = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b",
                 @Header("Content-Type") contentType: String = "application/json")
    : Call<MovieDetail>

    @GET
    fun getUrlBytes(@Url url: String): Call<ResponseBody>

}