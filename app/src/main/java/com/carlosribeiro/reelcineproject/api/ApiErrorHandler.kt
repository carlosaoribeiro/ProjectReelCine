// ApiErrorHandler.kt
package com.carlosribeiro.reelcineproject.api

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ApiErrorHandler {
    fun handleError(error: Throwable): String {
        return when (error) {
            is SocketTimeoutException -> "Timeout - Verifique sua conexão"
            is ConnectException -> "Servidor indisponível"
            is HttpException -> when (error.code()) {
                401 -> "Chave da API inválida ou expirou"
                404 -> "Recurso não encontrado"
                else -> "Erro HTTP ${error.code()}"
            }
            else -> "Erro inesperado: ${error.localizedMessage ?: "sem mensagem"}"
        }
    }
}
