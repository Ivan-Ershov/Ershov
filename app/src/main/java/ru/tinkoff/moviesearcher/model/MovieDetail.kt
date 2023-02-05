package ru.tinkoff.moviesearcher.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("kinopoiskId")
    var id: Int,
    var imdbId: String,
    var nameRu: String,
    var nameEn: String,
    var nameOriginal: String,
    var year: String,
    var filmLength: String,
    var countries: List<Country>,
    var genres: List<Genre>,
    @SerializedName("ratingKinopoisk")
    var rating: String,
    @SerializedName("ratingKinopoiskVoteCount")
    var ratingVoteCount: Int,
    var posterUrl: String,
    var posterUrlPreview: String,
    var coverUrl: String,
    var logoUrl: String,
    var reviewsCount: String,
    var ratingGoodReview: String,
    var ratingGoodReviewVoteCount: String,
    var ratingImdb: String,
    var ratingImdbVoteCount: String,
    var ratingFilmCritics: String,
    var ratingFilmCriticsVoteCount: String,
    var ratingAwait: String,
    var ratingAwaitCount: String,
    var ratingRfCritics: String,
    var ratingRfCriticsVoteCount: String,
    var webUrl: String,
    var slogan: String,
    var description: String,
    var shortDescription: String,
    var editorAnnotation: String,
    var isTicketsAvailable: Boolean,
    var productionStatus: String,
    var type: String,
    var ratingMpaa: String,
    var ratingAgeLimits: String,
    var startYear: String,
    var endYear: String,
    var serial: Boolean,
    var shortFilm: Boolean,
    var completed: Boolean,
    var hasImax: Boolean,
    var has3D: Boolean,
    var lastSync: String
)
