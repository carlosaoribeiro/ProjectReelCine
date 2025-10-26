package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityGruposBinding
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.carlosribeiro.reelcineproject.ui.recomendacao.MovieRatingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GruposActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGruposBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGruposBinding.inflate(layoutInflater)
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

        // Header do menu (avatar + nome)
        val headerView = binding.navigationView.getHeaderView(0)
        val avatarHeader = headerView.findViewById<ImageView>(R.id.imageUserAvatar)
        val nomeHeader = headerView.findViewById<TextView>(R.id.textUserName)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance().collection("usuarios").document(uid ?: "")
            .get()
            .addOnSuccessListener { doc ->
                val nome = doc.getString("nome") ?: "Usuário"
                val avatarUrl = doc.getString("avatarUrl")
                nomeHeader.text = nome
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .circleCrop()
                        .into(avatarHeader)
                }
            }

        // Menu lateral
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_feed -> startActivity(Intent(this, FeedActivity::class.java))
                R.id.nav_grupos -> { /* Já está aqui */ }
                R.id.nav_recomendacoes -> startActivity(Intent(this, MovieRatingActivity::class.java))
                R.id.nav_perfil -> startActivity(Intent(this, PerfilActivity::class.java))
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        // Exibir aviso de construção
        binding.textEmConstrucao.visibility = View.VISIBLE
    }
}
