package com.carlosribeiro.reelcineproject.model

import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.network.response.FilmeDto

fun FilmeDto.toUiModel(): FilmeUi {
    return FilmeUi(
        id = this.id,
        titulo = this.title,
        descricao = this.overview ?: "",
        posterPath = this.poster_path ?: "",
        backdropPath = this.backdrop_path ?: "",
        ano = this.release_date?.take(4) ?: ""
    )
}
