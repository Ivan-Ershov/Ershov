package ru.tinkoff.moviesearcher.model

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("filmId")
    var id: Int,
    var nameRu: String,
    var nameEn: String,
    var year: String,
    var filmLength: String,
    var countries: List<Country>,
    var genres: List<Genre>,
    var rating: String,
    var ratingVoteCount: Int,
    var posterUrl: String,
    var posterUrlPreview: String,
    var ratingChange: String
)