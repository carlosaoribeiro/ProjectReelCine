package com.carlosribeiro.reelcineproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ItemFilmeBuscaBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi

class FilmeBuscaAdapter(
    private var filmes: List<FilmeUi>,
    private val onItemClick: (FilmeUi) -> Unit
) : RecyclerView.Adapter<FilmeBuscaAdapter.FilmeBuscaViewHolder>() {

    inner class FilmeBuscaViewHolder(val binding: ItemFilmeBuscaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeBuscaViewHolder {
        val binding = ItemFilmeBuscaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmeBuscaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeBuscaViewHolder, position: Int) {
        val filme = filmes[position]
        holder.binding.apply {
            textTitulo.text = filme.titulo
            textAno.text = filme.ano
            Glide.with(root.context)
                .load("https://image.tmdb.org/t/p/w500${filme.posterPath}")
                .into(imagePoster)
            root.setOnClickListener { onItemClick(filme) }
        }
    }

    override fun getItemCount() = filmes.size

    fun submitList(novaLista: List<FilmeUi>?) {
        filmes = novaLista ?: emptyList()
        notifyDataSetChanged()
    }
}
