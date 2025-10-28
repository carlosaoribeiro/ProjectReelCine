package com.carlosribeiro.reelcineproject.ui.detalhes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ActivityDetalhesFilmeBinding

class DetalhesFilmeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesFilmeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titulo = intent.getStringExtra("titulo")
        val descricao = intent.getStringExtra("descricao")
        val posterPath = intent.getStringExtra("posterPath")
        val ano = intent.getStringExtra("ano")

        binding.textTituloFilme.text = titulo
        binding.textDescricao.text = descricao
        binding.textAnoDuracao.text = "Lançamento: $ano"

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(binding.imagePosterDetalhes)
    }
}
