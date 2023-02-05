package ru.tinkoff.moviesearcher.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country")
    var name: String
)
