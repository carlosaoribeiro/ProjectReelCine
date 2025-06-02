package com.carlosribeiro.reelcineproject.ui

import com.carlosribeiro.reelcineproject.viewmodel.MainViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityMainBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        binding.progressLoading.visibility = View.VISIBLE

        // Header do menu com avatar + nome
        val headerView = binding.navigationView.getHeaderView(0)
        val imageUserAvatar = headerView.findViewById<ImageView>(R.id.imageUserAvatar)
        val textUserName = headerView.findViewById<TextView>(R.id.textUserName)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val nome = doc.getString("nome") ?: "Usuário"
                    val avatarUrl = doc.getString("avatarUrl")

                    textUserName.text = nome

                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(avatarUrl)
                            .circleCrop()
                            .into(imageUserAvatar)
                    }
                }
        }

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

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Log.d("NAV", "Clicou em Home")
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_feed -> {
                    Log.d("NAV", "Clicou em Feed")
                    startActivity(Intent(this, FeedActivity::class.java))
                    true
                }
                R.id.nav_grupos -> {
                    Log.d("NAV", "Clicou em Grupos")
                    startActivity(Intent(this, GruposActivity::class.java))
                    true
                }
                R.id.nav_recomendacoes -> {
                    Log.d("NAV", "Clicou em Recomendações")
                    startActivity(Intent(this, RecomendarFilmeActivity::class.java))
                    true
                }
                R.id.nav_perfil -> {
                    Log.d("NAV", "Clicou em Meu Perfil")
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    Log.d("NAV", "Clicou Logout")
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
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
            binding.progressLoading.visibility = View.GONE
            val adapter = FilmeHorizontalAdapter(filmes) { filme -> abrirDetalhesFilme(filme) }
            binding.recyclerViewTrending.adapter = adapter
        }

        // Carrossel 2 - Novidades
        binding.recyclerViewNovidades.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesNovidades.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme -> abrirDetalhesFilme(filme) }
            binding.recyclerViewNovidades.adapter = adapter
        }

        // Carrossel 3 - Lançamentos
        binding.recyclerViewLancamentos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesLancamentos.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme -> abrirDetalhesFilme(filme) }
            binding.recyclerViewLancamentos.adapter = adapter
        }

        // Carrossel 4 - Top Avaliados
        binding.recyclerViewTopAvaliados.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesTopAvaliados.observe(this) { filmes ->
            val adapter = FilmeHorizontalAdapter(filmes) { filme -> abrirDetalhesFilme(filme) }
            binding.recyclerViewTopAvaliados.adapter = adapter
        }

        viewModel.carregarFilmesTrending()
        viewModel.carregarFilmesNovidades()
        viewModel.carregarFilmesLancamentos()
        viewModel.carregarFilmesTopAvaliados()
    }

    private fun abrirDetalhesFilme(filme: FilmeUi) {
        val intent = Intent(this, FilmeDetailsActivity::class.java).apply {
            putExtra("id", filme.id)
            putExtra("titulo", filme.titulo)
            putExtra("descricao", filme.descricao)
            putExtra("posterPath", filme.posterPath)
            putExtra("backdropPath", filme.backdropPath)
            putExtra("ano", filme.ano)
        }
        startActivity(intent)
    }
}
