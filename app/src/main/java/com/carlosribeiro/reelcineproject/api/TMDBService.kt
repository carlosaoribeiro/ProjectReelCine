package com.carlosribeiro.reelcineproject.api

import com.carlosribeiro.reelcineproject.BuildConfig
import com.carlosribeiro.reelcineproject.model.FilmeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "8fdc23c3632ca01c557a2aec34674d9f",
        @Query("language") language: String = "pt-BR",
        @Query("page") page: Int = 1
    ): Response<FilmeResponse>
}
