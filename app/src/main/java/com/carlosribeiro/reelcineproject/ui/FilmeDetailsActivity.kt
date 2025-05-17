package com.carlosribeiro.reelcineproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ActivityDetalhesFilmeBinding

class FilmeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesFilmeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera dados do intent
        val titulo = intent.getStringExtra("titulo") ?: "Sem título"
        val descricao = intent.getStringExtra("descricao") ?: "Sem descrição"
        val posterPath = intent.getStringExtra("posterPath") ?: ""
        val backdropPath = intent.getStringExtra("backdropPath") ?: ""
        val ano = intent.getStringExtra("ano") ?: "Ano desconhecido"

        // Popula os campos da tela
        binding.textTituloFilme.text = titulo
        binding.textDescricao.text = descricao
        binding.textAnoDuracao.text = "Lançamento: $ano"

        // Carrega imagem do poster
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(binding.imagePosterDetalhes)

        // Carrega imagem de fundo (backdrop)
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w780$backdropPath")
            .into(binding.imageCapa)

        // Botão voltar
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}
