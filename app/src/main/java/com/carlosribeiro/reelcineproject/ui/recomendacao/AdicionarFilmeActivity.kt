package com.carlosribeiro.reelcineproject.ui.recomendacao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosribeiro.reelcineproject.api.RetrofitClient
import com.carlosribeiro.reelcineproject.databinding.ActivityAdicionarFilmeBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.ui.adapter.FilmeAdapter
import com.carlosribeiro.reelcineproject.ui.detalhes.DetalhesFilmeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class AdicionarFilmeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdicionarFilmeBinding
    private lateinit var adapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdicionarFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Configura RecyclerView e Adapter
        adapter = FilmeAdapter { filme ->
            // Ao clicar em “Selecionar”, abre a tela de detalhes (ou de avaliação)
            val intent = Intent(this, DetalhesFilmeActivity::class.java)
            intent.putExtra("id", filme.id)
            intent.putExtra("titulo", filme.titulo)
            intent.putExtra("descricao", filme.descricao)
            intent.putExtra("posterPath", filme.posterPath)
            intent.putExtra("ano", filme.ano)
            startActivity(intent)
        }

        binding.recyclerFilmes.layoutManager = LinearLayoutManager(this)
        binding.recyclerFilmes.adapter = adapter

        // ✅ Botão de buscar
        binding.btnBuscarFilme.setOnClickListener {
            val titulo = binding.editTextTituloFilme.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "Digite o título do filme", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buscarFilme(titulo)
        }

        // ✅ Recebe dados da tela anterior (caso tenha vindo via Intent)
        val tituloRecebido = intent.getStringExtra("titulo_predefinido")
        val deveExecutarBusca = intent.getBooleanExtra("executar_busca", false)

        if (!tituloRecebido.isNullOrEmpty()) {
            binding.editTextTituloFilme.setText(tituloRecebido)
            if (deveExecutarBusca) {
                binding.btnBuscarFilme.post { binding.btnBuscarFilme.performClick() }
            }
        }
    }

    // 🔍 Busca filme na API TMDB
    private fun buscarFilme(titulo: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerFilmes.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.buscarFilmes(titulo)
                }

                binding.progressBar.visibility = View.GONE

                if (!response.results.isNullOrEmpty()) {
                    // Converte resultado da API em lista de FilmeUi (se já não for)
                    val listaUi = response.results.map { filme ->
                        FilmeUi(
                            id = filme.id,
                            titulo = filme.title ?: "Sem título",
                            descricao = filme.overview ?: "",
                            posterPath = filme.poster_path.toString(),
                            ano = filme.release_date?.take(4) ?: "N/A",
                            backdropPath = filme.backdrop_path ?: ""
                        )
                    }
                    adapter.submitList(listaUi)
                    binding.recyclerFilmes.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@AdicionarFilmeActivity, "Nenhum filme encontrado.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@AdicionarFilmeActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}
