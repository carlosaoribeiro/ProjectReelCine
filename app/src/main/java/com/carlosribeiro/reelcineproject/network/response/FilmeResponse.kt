package com.carlosribeiro.reelcineproject.network.response

import com.carlosribeiro.reelcineproject.model.FilmeDto

data class FilmeResponse(
    val results: List<FilmeDto>
)
