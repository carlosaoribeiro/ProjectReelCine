package com.carlosribeiro.reelcineproject.model

data class Recomendacao(
    val titulo: String = "",
    val comentario: String = "",
    val posterPath: String = "",
    val autor: String = "",
    val usuarioNome: String = "",
    val timestamp: Long = 0L,
    val avatarUrl: String = "",
    val averageRating: Float = 0f, // ✅ média das notas
    val ratingsCount: Int = 0,      // ✅ número de avaliações
    val rating: Float = 0f
)

