package com.carlosribeiro.reelcineproject.ui

import com.carlosribeiro.reelcineproject.viewmodel.MainViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityMainBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.google.firebase.auth.FirebaseAuth
import com.carlosribeiro.reelcineproject.ui.main.FilmeHorizontalAdapter
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar
        setSupportActionBar(binding.toolbar)

        // Habilita o ícone de menu (hambúrguer)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Listener do menu lateral
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    true
                }

                R.id.nav_feed -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    true
                }
                R.id.nav_grupos -> {
                    startActivity(Intent(this, GruposActivity::class.java))
                    true
                }
                R.id.nav_recomendacoes -> {
                    startActivity(Intent(this, RecomendarFilmeActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut() // <-- Faz logout real
                    startActivity(Intent(this, LoginActivity::class.java)) // redireciona para tela de login
                    finishAffinity() // encerra tudo
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawers()
            }
        }

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

        // Carrega os dados
        viewModel.carregarFilmesTrending()
        viewModel.carregarFilmesNovidades()
        viewModel.carregarFilmesLancamentos()
        viewModel.carregarFilmesTopAvaliados()
    }

    private fun abrirDetalhesFilme(filme: FilmeUi) {
        val intent = Intent(this, FilmeDetailsActivity::class.java).apply {
            putExtra("titulo", filme.titulo)
            putExtra("descricao", filme.descricao)
            putExtra("posterPath", filme.posterPath)
            putExtra("backdropPath", filme.backdropPath)
            putExtra("ano", filme.ano)
        }
        startActivity(intent)
    }
}
