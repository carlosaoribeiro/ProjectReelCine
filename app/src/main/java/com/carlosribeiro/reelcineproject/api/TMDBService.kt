package com.carlosribeiro.reelcineproject.api

import com.carlosribeiro.reelcineproject.network.response.FilmeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.carlosribeiro.reelcineproject.BuildConfig
import com.carlosribeiro.reelcineproject.network.response.FilmeVideoResponse
import retrofit2.Response
import retrofit2.http.Path

interface TMDBService {

    @GET("trending/movie/day")
    suspend fun buscarFilmesEmAlta(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): FilmeResponse

    @GET("movie/now_playing")
    suspend fun buscarFilmesRecentes(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "pt-BR"
    ): FilmeResponse

    @GET("movie/upcoming")
    suspend fun buscarFilmesLancamentos(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "pt-BR"
    ): FilmeResponse

    @GET("movie/top_rated")
    suspend fun buscarFilmesTopAvaliados(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "pt-BR"
    ): FilmeResponse

    @GET("search/movie")
    suspend fun buscarFilmes(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "pt-BR"
    ): FilmeResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<FilmeVideoResponse>
}
