package com.carlosribeiro.reelcineproject.model

data class FilmeResponse(
    val results: List<Filme>
)

data class Filme(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String
)
