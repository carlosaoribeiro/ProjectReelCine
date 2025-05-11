package com.carlosribeiro.reelcineproject.model

data class FilmeResponse(
    val page: Int,
    val results: List<Filme>
)
