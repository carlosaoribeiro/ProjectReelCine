package com.carlosribeiro.reelcineproject.model.mapper

import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.model.FilmeDto

fun FilmeDto.toUiModel(): FilmeUi {
    return FilmeUi(
        id = id,
        titulo = title ?: "Sem título",
        descricao = overview ?: "",
        posterPath = poster_path ?: "",
        backdropPath = backdrop_path ?: "",
        ano = release_date?.take(4) ?: "N/A"
    )
}
