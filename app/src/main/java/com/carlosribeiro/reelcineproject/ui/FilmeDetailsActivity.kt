package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.api.RetrofitClient
import com.carlosribeiro.reelcineproject.databinding.ActivityDetalhesFilmeBinding
import com.carlosribeiro.reelcineproject.ui.recomendacao.mapper.toUiModel
import kotlinx.coroutines.launch

class FilmeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesFilmeBinding
    private var filmeId: Int = -1 // Variável da classe para guardar o ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        // Botão voltar
        binding.btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Dados do Intent (pegamos o ID aqui, uma única vez)
        filmeId = intent.getIntExtra("id", -1)
        val titulo = intent.getStringExtra("titulo") ?: "Sem título"
        val descricao = intent.getStringExtra("descricao") ?: "Sem descrição"
        val posterPath = intent.getStringExtra("posterPath") ?: ""
        val backdropPath = intent.getStringExtra("backdropPath") ?: ""
        val ano = intent.getStringExtra("ano") ?: "Ano desconhecido"

        // Preenche UI
        binding.textTituloFilme.text = titulo
        binding.textDescricao.text = descricao
        binding.textAnoDuracao.text = "Lançamento: $ano"

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(binding.imagePosterDetalhes)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w780$backdropPath")
            .into(binding.imageCapa)

        // Botão trailer (agora usa a variável da classe)
        binding.btnTrailer.setOnClickListener {
            if (filmeId != -1) {
                buscarTrailer(filmeId)
            } else {
                Toast.makeText(this, "ID do filme é inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun buscarTrailer(filmeId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getVideos(filmeId)
                if (response.isSuccessful) {
                    val trailers = response.body()?.results.orEmpty()
                        .mapNotNull { it.toUiModel() }

                    val trailer = trailers.firstOrNull()

                    trailer?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.urlYoutube))
                        startActivity(intent)
                    } ?: Toast.makeText(this@FilmeDetailsActivity, "Trailer não encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FilmeDetailsActivity, "Erro ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@FilmeDetailsActivity, "Falha ao carregar trailer", Toast.LENGTH_SHORT).show()
            }
        }
    }
}