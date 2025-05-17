package com.carlosribeiro.reelcineproject.model

import com.google.gson.annotations.SerializedName

data class Filme(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("release_date")
    val releaseDate: String
)

