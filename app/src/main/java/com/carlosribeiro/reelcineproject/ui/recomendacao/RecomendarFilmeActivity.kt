package com.carlosribeiro.reelcineproject.ui.recomendacao

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityRecomendarFilmeBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.GruposActivity
import com.carlosribeiro.reelcineproject.ui.MainActivity
import com.carlosribeiro.reelcineproject.ui.adapter.FilmeBuscaAdapter
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.carlosribeiro.reelcineproject.viewmodel.BuscarFilmeViewModel

class RecomendarFilmeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendarFilmeBinding
    private lateinit var viewModel: BuscarFilmeViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var adapter: FilmeBuscaAdapter
    private var filmeSelecionado: FilmeUi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendarFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Menu lateral completo
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
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
                    Toast.makeText(this, "Você já está aqui", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_logout -> {
                    finishAffinity()
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawers()
            }
        }

        viewModel = ViewModelProvider(this)[BuscarFilmeViewModel::class.java]

        adapter = FilmeBuscaAdapter(
            filmes = emptyList(),
            onItemClick = { filme ->
                filmeSelecionado = filme
                binding.textFilmeSelecionado.text = "Filme selecionado: ${filme.titulo}"
            }
        )

        binding.recyclerViewFilmes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFilmes.adapter = adapter

        binding.btnBuscar.setOnClickListener {
            val query = binding.editBusca.text.toString()
            if (query.isNotBlank()) {
                viewModel.buscarFilmes(query)
            } else {
                Toast.makeText(this, "Digite o nome do filme", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.resultado.observe(this) { filmes ->
            Log.d("RecomendarFilme", "Filmes encontrados: ${filmes.size}")
            adapter.submitList(null)
            adapter.submitList(filmes)
        }

        binding.btnSalvarRecomendacao.setOnClickListener {
            val comentario = binding.editComentario.text.toString()
            val filme = filmeSelecionado
            if (filme != null && comentario.isNotBlank()) {
                Log.d("RecomendarFilme", "Recomendado: ${filme.titulo} - $comentario")
                Toast.makeText(this, "Filme recomendado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Selecione um filme e digite um comentário", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
