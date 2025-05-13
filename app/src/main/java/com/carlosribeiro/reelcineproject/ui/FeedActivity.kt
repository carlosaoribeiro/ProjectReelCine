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
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import com.carlosribeiro.reelcineproject.viewmodel.RecomendacoesViewModel

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: RecomendacoesViewModel
    private lateinit var adapter: RecomendacaoAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FeedActivity", "onCreate iniciado")
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
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
                R.id.nav_feed -> { /* TODO: implementar */ }
                R.id.nav_grupos -> { /* TODO: implementar */ }
                R.id.nav_recomendacoes -> {
                    startActivity(Intent(this, RecomendarFilmeActivity::class.java))
                }
                R.id.nav_logout -> { /* TODO: implementar */ }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        adapter = RecomendacaoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabRecomendar.setOnClickListener {
            startActivity(Intent(this, RecomendarFilmeActivity::class.java))
        }

        viewModel = ViewModelProvider(this)[RecomendacoesViewModel::class.java]

        viewModel.recomendacoes.observe(this) { lista ->
            Log.d("FeedActivity", "Lista recebida: \${lista.size}")
            adapter.submitList(null)
            adapter.submitList(lista)
        }

        viewModel.buscarTodasRecomendacoes()
    }

    override fun onResume() {
        super.onResume()
        viewModel.buscarTodasRecomendacoes()
    }
}
