package ru.tinkoff.moviesearcher.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genre")
    var name: String
)
