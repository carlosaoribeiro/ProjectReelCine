package com.carlosribeiro.reelcineproject.api

import com.carlosribeiro.reelcineproject.model.FilmeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR"
    ): Call<FilmeResponse>
}
