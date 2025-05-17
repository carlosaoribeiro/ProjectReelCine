package com.carlosribeiro.reelcineproject.model

data class Recomendacao(
    val titulo: String = "",
    val comentario: String = "",
    val posterPath: String = "",
    val avatarUrl: String = "",
    val autor: String = "",
    val usuarioNome: String = "",
    val timestamp: Long = 0L // ✅ continua como Long
)

