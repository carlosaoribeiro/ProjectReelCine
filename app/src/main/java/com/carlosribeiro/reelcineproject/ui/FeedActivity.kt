package com.carlosribeiro.reelcineproject.ui

import android.net.http.UrlRequest
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.adapter.FilmeAdapter
import com.carlosribeiro.reelcineproject.api.RetrofitClient
import com.carlosribeiro.reelcineproject.model.Filme
import com.carlosribeiro.reelcineproject.model.FilmeResponse
import com.google.android.gms.common.api.Response
import kotlinx.android.synthetic.main.activity_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val apiKey = "YOUR_TMDB_API_KEY" // Substitua pela sua chave da TMDB

        RetrofitClient.instance.getPopularMovies(apiKey)
            .enqueue(object : UrlRequest.Callback<FilmeResponse> {
                override fun onResponse(call: Call<FilmeResponse>, response: Response<FilmeResponse>) {
                    if (response.isSuccessful) {
                        val filmes = response.body()?.results ?: emptyList()
                        setupRecyclerView(filmes)
                    } else {
                        Toast.makeText(this@FeedActivity, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<FilmeResponse>, t: Throwable) {
                    Toast.makeText(this@FeedActivity, "Falha na requisição", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupRecyclerView(filmes: List<Filme>) {
        filmesRecyclerView.layoutManager = LinearLayoutManager(this)
        filmesRecyclerView.adapter = FilmeAdapter(filmes)
    }
}
