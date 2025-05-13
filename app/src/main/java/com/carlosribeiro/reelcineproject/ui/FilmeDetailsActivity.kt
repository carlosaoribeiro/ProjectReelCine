package com.carlosribeiro.reelcineproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ActivityDetalhesFilmeBinding

class FilmeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesFilmeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titulo = intent.getStringExtra("titulo")
        val descricao = intent.getStringExtra("descricao")
        val posterPath = intent.getStringExtra("posterPath")
        val ano = intent.getStringExtra("ano")

        binding.btnVoltarHome.setOnClickListener {
            Toast.makeText(this, "Voltando para a home...", Toast.LENGTH_SHORT).show();
            finish()
        }

        if (titulo != null && descricao != null && posterPath != null && ano != null) {
            binding.textTituloDetalhes.text = titulo
            binding.textDescricaoDetalhes.text = descricao
            binding.textAnoDetalhes.text = "Lançamento: $ano"

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500$posterPath")
                .into(binding.imagePosterDetalhes)

            binding.btnVoltarHome.setOnClickListener {
                finish()
            }
        } else {
            Toast.makeText(this, "Erro ao carregar detalhes do filme", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
