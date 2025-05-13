package com.carlosribeiro.reelcineproject.model.mapper

import com.carlosribeiro.reelcineproject.model.Filme
import com.carlosribeiro.reelcineproject.model.FilmeUi

fun Filme.toUiModel(): FilmeUi {
    return FilmeUi(
        id = this.id,
        titulo = this.title,
        descricao = this.overview,
        imagemUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        ano = this.releaseDate.take(4)
    )
}
