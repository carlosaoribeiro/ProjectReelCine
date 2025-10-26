package com.carlosribeiro.reelcineproject.ui.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityFeedBinding
import com.carlosribeiro.reelcineproject.ui.*
import com.carlosribeiro.reelcineproject.ui.recomendacao.MovieRatingActivity
import com.carlosribeiro.reelcineproject.viewmodel.FeedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: RecomendacaoAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔧 Toolbar e Drawer
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

        // 💡 Header do menu com avatar e nome
        val headerView = binding.navigationView.getHeaderView(0)
        val avatarImage = headerView.findViewById<ImageView>(R.id.imageUserAvatar)
        val userNameText = headerView.findViewById<TextView>(R.id.textUserName)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    userNameText.text = doc.getString("nome") ?: "Usuário"
                    val avatarUrl = doc.getString("avatarUrl")
                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(avatarUrl)
                            .circleCrop()
                            .into(avatarImage)
                    }
                }
        }

        // ☰ Menu lateral
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_feed -> true // já está aqui
                R.id.nav_grupos -> {
                    startActivity(Intent(this, GruposActivity::class.java))
                    true
                }
                R.id.nav_recomendacoes -> {
                    startActivity(Intent(this, MovieRatingActivity::class.java))
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

        // 📰 Lista de recomendações
        adapter = RecomendacaoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // ➕ FAB
        binding.fabRecomendar.setOnClickListener {
            startActivity(Intent(this, MovieRatingActivity::class.java))
        }

        // 📡 ViewModel
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        binding.progressFeed.visibility = View.VISIBLE
        viewModel.recomendacoes.observe(this) { lista ->
            Log.d("FeedActivity", "Lista recebida: ${lista.size}")
            binding.progressFeed.visibility = View.GONE
            adapter.submitList(lista)
        }

        viewModel.observarRecomendacoesEmTempoReal()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observarRecomendacoesEmTempoReal()
    }
}
