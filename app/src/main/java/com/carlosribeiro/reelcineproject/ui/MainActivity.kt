package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityMainBinding
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.carlosribeiro.reelcineproject.ui.grupos.GruposActivity
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)

        // Drawer toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Menu lateral
        binding.navView.setNavigationItemSelectedListener(this)

        // Clique no botão de recomendações
        val btnRecomendar = findViewById<Button>(R.id.btnAbrirRecomendacoes)
        btnRecomendar.setOnClickListener {
            startActivity(Intent(this, RecomendarFilmeActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_feed -> startActivity(Intent(this, FeedActivity::class.java))
            R.id.nav_grupos -> startActivity(Intent(this, GruposActivity::class.java))
            R.id.nav_recomendacoes -> startActivity(Intent(this, RecomendarFilmeActivity::class.java))
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
