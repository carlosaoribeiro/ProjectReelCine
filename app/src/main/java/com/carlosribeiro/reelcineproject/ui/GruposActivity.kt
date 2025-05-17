package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityGruposBinding
import com.carlosribeiro.reelcineproject.ui.feed.FeedActivity
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import kotlin.jvm.java

class GruposActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGruposBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGruposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar com botão de menu lateral (hambúrguer)
        setSupportActionBar(binding.toolbar)

        // Drawer toggle (menu lateral)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Menu lateral
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feed -> {
                    startActivity(Intent(this, FeedActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_grupos -> {
                    // Já está na tela de grupos
                    binding.drawerLayout.closeDrawers()
                    true
                }

                R.id.nav_recomendacoes -> {
                    startActivity(Intent(this, RecomendarFilmeActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_logout -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}
