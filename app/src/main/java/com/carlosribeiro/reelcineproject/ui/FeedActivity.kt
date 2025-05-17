package com.carlosribeiro.reelcineproject.ui.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityFeedBinding
import com.carlosribeiro.reelcineproject.ui.GruposActivity
import com.carlosribeiro.reelcineproject.ui.LoginActivity
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import com.carlosribeiro.reelcineproject.viewmodel.FeedViewModel

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: RecomendacaoAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)

        // Drawer toggle
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
                    binding.drawerLayout.closeDrawers()
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
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // RecyclerView
        adapter = RecomendacaoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Botão flutuante
        binding.fabRecomendar.setOnClickListener {
            startActivity(Intent(this, RecomendarFilmeActivity::class.java))
        }

        // ViewModel
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        viewModel.recomendacoes.observe(this) { lista ->
            Log.d("FeedActivity", "Lista recebida: ${lista.size}")
            adapter.submitList(null)
            adapter.submitList(lista)
        }

        viewModel.observarRecomendacoesEmTempoReal()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observarRecomendacoesEmTempoReal()
    }
}
