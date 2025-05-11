package com.carlosribeiro.reelcineproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.databinding.ActivityFeedBinding
import com.carlosribeiro.reelcineproject.ui.adapter.FilmeAdapter
import com.carlosribeiro.reelcineproject.viewmodel.FeedViewModel

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private val viewModel: FeedViewModel by viewModels()
    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        viewModel.fetchMovies()
    }

    private fun setupRecyclerView() {
        filmeAdapter = FilmeAdapter(emptyList())
        binding.filmesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = filmeAdapter
        }
    }

    private fun setupObservers() {
        viewModel.filmes.observe(this) { filmes ->
            filmeAdapter.updateList(filmes)
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}
