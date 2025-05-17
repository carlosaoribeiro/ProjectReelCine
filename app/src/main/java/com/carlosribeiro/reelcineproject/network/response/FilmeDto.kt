package com.carlosribeiro.reelcineproject.network.response

data class FilmeDto(
    val id: Int,
    val title: String,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?
)