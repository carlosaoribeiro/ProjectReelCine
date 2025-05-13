package com.carlosribeiro.reelcineproject.ui.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.databinding.ActivityFeedBinding
import com.carlosribeiro.reelcineproject.ui.recomendacao.RecomendarFilmeActivity
import com.carlosribeiro.reelcineproject.viewmodel.RecomendacoesViewModel

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: RecomendacoesViewModel
    private lateinit var adapter: RecomendacaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FeedActivity", "onCreate iniciado")
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecomendacaoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabRecomendar.setOnClickListener {
            startActivity(Intent(this, RecomendarFilmeActivity::class.java))
        }

        viewModel = ViewModelProvider(this)[RecomendacoesViewModel::class.java]

        // Observa a lista uma única vez
        viewModel.recomendacoes.observe(this) { lista ->
            Log.d("FeedActivity", "Lista recebida: ${lista.size}")
            adapter.submitList(null)
            adapter.submitList(lista)

        }

        // Primeira carga
        viewModel.buscarTodasRecomendacoes()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega a lista sempre que voltar
        viewModel.buscarTodasRecomendacoes()
    }
}
