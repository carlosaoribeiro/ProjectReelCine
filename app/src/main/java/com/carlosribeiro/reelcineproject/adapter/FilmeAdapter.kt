package com.carlosribeiro.reelcineproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.carlosribeiro.reelcineproject.databinding.ItemFilmeBinding
import com.carlosribeiro.reelcineproject.model.Filme

class FilmeAdapter(
    private var filmes: List<Filme>
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    inner class FilmeViewHolder(private val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filme: Filme) {
            binding.textTitulo.text = filme.title
            // Se quiser carregar imagem com o Coil:
            binding.imageFilme.load("https://image.tmdb.org/t/p/w500${filme.posterPath}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(filmes[position])
    }

    override fun getItemCount() = filmes.size

    // ✅ Adicione este método
    fun updateList(novosFilmes: List<Filme>) {
        filmes = novosFilmes
        notifyDataSetChanged()
    }
}
