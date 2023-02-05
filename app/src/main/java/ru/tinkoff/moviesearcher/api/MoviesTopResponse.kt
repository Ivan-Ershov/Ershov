package ru.tinkoff.moviesearcher.api

import com.google.gson.annotations.SerializedName
import ru.tinkoff.moviesearcher.model.Movie

class MoviesTopResponse {
    var pagesCount: Int? = null
    @SerializedName("films")
    lateinit var movies: List<Movie>
}
