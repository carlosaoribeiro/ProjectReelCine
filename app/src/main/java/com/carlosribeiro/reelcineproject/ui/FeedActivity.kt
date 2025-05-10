package com.carlosribeiro.reelcineproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.api.RetrofitClient
import com.carlosribeiro.reelcineproject.databinding.ActivityFeedBinding
import com.carlosribeiro.reelcineproject.model.Filme
import com.carlosribeiro.reelcineproject.model.FilmeResponse
import com.carlosribeiro.reelcineproject.ui.adapter.FilmeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiKey = "YOUR_TMDB_API_KEY"

        RetrofitClient.instance.getPopularMovies(apiKey)
            .enqueue(object : Callback<FilmeResponse> {
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
        binding.filmesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.filmesRecyclerView.adapter = FilmeAdapter(filmes)
    }
}
