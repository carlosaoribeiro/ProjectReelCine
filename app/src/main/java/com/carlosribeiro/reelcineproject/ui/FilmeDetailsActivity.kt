package com.carlosribeiro.reelcineproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ActivityFilmeDetailsBinding

class FilmeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titulo = intent.getStringExtra("titulo")
        val descricao = intent.getStringExtra("descricao")
        val posterPath = intent.getStringExtra("posterPath")
        val ano = intent.getStringExtra("ano")

        binding.textTitulo.text = titulo
        binding.textDescricao.text = descricao
        binding.textAno.text = "Ano: $ano"

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$posterPath")
            .into(binding.imagePoster)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}
