package com.carlosribeiro.reelcineproject.api

import android.util.Log
import com.carlosribeiro.reelcineproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // 🔐 Interceptor para adicionar o Bearer Token
    private val authInterceptor = Interceptor { chain ->
        val token = BuildConfig.TMDB_API_KEY
        Log.d("INTERCEPTOR", "Token enviado: $token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    // 🛠 Cliente com autenticação e logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // 🧱 Instância Retrofit com client customizado
    val instance: TMDBService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBService::class.java)
    }
}
