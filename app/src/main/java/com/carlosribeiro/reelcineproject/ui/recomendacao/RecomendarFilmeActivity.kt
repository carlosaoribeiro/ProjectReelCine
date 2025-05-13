package com.carlosribeiro.reelcineproject.ui.recomendacao

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.databinding.ActivityRecomendarFilmeBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.viewmodel.BuscarFilmeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp


class RecomendarFilmeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendarFilmeBinding
    private lateinit var viewModel: BuscarFilmeViewModel
    private lateinit var adapter: FilmeAdapter
    private var filmeSelecionado: FilmeUi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendarFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BuscarFilmeViewModel::class.java]

        adapter = FilmeAdapter { filme ->
            filmeSelecionado = filme
            binding.textFilmeSelecionado.text = "Selecionado: ${filme.titulo}"
        }

        binding.recyclerViewFilmes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFilmes.adapter = adapter

        viewModel.resultado.observe(this) { filmes ->
            adapter.submitList(filmes)
        }

        binding.btnBuscar.setOnClickListener {
            val termo = binding.editBusca.text.toString()
            if (termo.isNotBlank()) {
                viewModel.buscarFilmes(termo)
            }
        }

        binding.btnSalvarRecomendacao.setOnClickListener {
            val comentario = binding.editComentario.text.toString()
            val filme = filmeSelecionado
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            if (filme != null && comentario.isNotBlank()) {
                val recomendacao = hashMapOf(
                    "titulo" to filme.titulo,
                    "posterPath" to filme.imagemUrl,
                    "comentario" to comentario,
                    "timestamp" to System.currentTimeMillis()
                )

                FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .collection("recomendacoes")
                    .add(recomendacao)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Filme recomendado!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                Toast.makeText(this, "Preencha tudo antes de recomendar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}