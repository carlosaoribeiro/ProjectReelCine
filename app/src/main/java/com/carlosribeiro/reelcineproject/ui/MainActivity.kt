package com.carlosribeiro.reelcineproject.ui

import MainViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.databinding.ActivityMainBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.main.FilmeHorizontalAdapter
import com.carlosribeiro.reelcineproject.ui.FilmeDetailsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Carrossel 1 - Em Alta
        binding.recyclerViewTrending.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesTrending.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme ->
                abrirDetalhesFilme(filme)
            }
            binding.recyclerViewTrending.adapter = adapter
        }

        // Carrossel 2 - Novidades
        binding.recyclerViewNovidades.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesNovidades.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme ->
                abrirDetalhesFilme(filme)
            }
            binding.recyclerViewNovidades.adapter = adapter
        }

        // Carrossel 3 - Lançamentos
        binding.recyclerViewLancamentos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesLancamentos.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme ->
                abrirDetalhesFilme(filme)
            }
            binding.recyclerViewLancamentos.adapter = adapter
        }

        // Carrossel 4 - Top Avaliados
        binding.recyclerViewTopAvaliados.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesTopAvaliados.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme ->
                abrirDetalhesFilme(filme)
            }
            binding.recyclerViewTopAvaliados.adapter = adapter
        }

        // Chamada das funções do ViewModel
        viewModel.carregarFilmesTrending()
        viewModel.carregarFilmesNovidades()
        viewModel.carregarFilmesLancamentos()
        viewModel.carregarFilmesTopAvaliados()
    }

    private fun abrirDetalhesFilme(filme: FilmeUi) {
        val intent = Intent(this, FilmeDetailsActivity::class.java).apply {
            putExtra("titulo", filme.titulo)
            putExtra("descricao", filme.descricao)
            putExtra("posterPath", filme.imagemUrl)
            putExtra("ano", filme.ano)
        }
        startActivity(intent)
    }
}
