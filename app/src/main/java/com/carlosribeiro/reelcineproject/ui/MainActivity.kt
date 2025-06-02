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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressLoading.visibility = View.VISIBLE
        atualizarHeaderUsuario()

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
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
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
                R.id.nav_perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
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

        binding.recyclerViewTrending.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesTrending.observe(this) {
            binding.progressLoading.visibility = View.GONE
            binding.recyclerViewTrending.adapter = FilmeHorizontalAdapter(it) { filme -> abrirDetalhesFilme(filme) }
        }

        binding.recyclerViewNovidades.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesNovidades.observe(this) {
            binding.recyclerViewNovidades.adapter = FilmeHorizontalAdapter(it) { filme -> abrirDetalhesFilme(filme) }
        }

        binding.recyclerViewLancamentos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesLancamentos.observe(this) {
            binding.recyclerViewLancamentos.adapter = FilmeHorizontalAdapter(it) { filme -> abrirDetalhesFilme(filme) }
        }

        binding.recyclerViewTopAvaliados.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filmesTopAvaliados.observe(this) {
            binding.recyclerViewTopAvaliados.adapter = FilmeHorizontalAdapter(it) { filme -> abrirDetalhesFilme(filme) }
        }

        viewModel.carregarFilmesTrending()
        viewModel.carregarFilmesNovidades()
        viewModel.carregarFilmesLancamentos()
        viewModel.carregarFilmesTopAvaliados()
    }

    private fun atualizarHeaderUsuario() {
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
    }

    override fun onResume() {
        super.onResume()
        atualizarHeaderUsuario()
    }

    private fun abrirDetalhesFilme(filme: FilmeUi) {
        startActivity(Intent(this, FilmeDetailsActivity::class.java).apply {
            putExtra("id", filme.id)
            putExtra("titulo", filme.titulo)
            putExtra("descricao", filme.descricao)
            putExtra("posterPath", filme.posterPath)
            putExtra("backdropPath", filme.backdropPath)
            putExtra("ano", filme.ano)
        })
    }
}
