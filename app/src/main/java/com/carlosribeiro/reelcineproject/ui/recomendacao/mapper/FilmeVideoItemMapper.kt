package com.carlosribeiro.reelcineproject.ui.recomendacao.mapper

import com.carlosribeiro.reelcineproject.network.response.FilmeVideoItem
import com.carlosribeiro.reelcineproject.ui.recomendacao.model.TrailerUiModel

fun FilmeVideoItem.toUiModel(): TrailerUiModel? {
    return if (site == "YouTube" && type == "Trailer") {
        TrailerUiModel(
            titulo = name,
            urlYoutube = "https://www.youtube.com/watch?v=$key"
        )
    } else {
        null
    }
}
