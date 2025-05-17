package com.carlosribeiro.reelcineproject.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ItemFilmeHorizontalBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi

class FilmeHorizontalAdapter(
    private val filmes: List<FilmeUi>,
    private val onClick: (FilmeUi) -> Unit
) : RecyclerView.Adapter<FilmeHorizontalAdapter.FilmeViewHolder>() {

    inner class FilmeViewHolder(val binding: ItemFilmeHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: FilmeUi) {
            binding.textTitulo.text = filme.titulo

            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${filme.posterPath}")
                .into(binding.imagePoster)

            binding.root.setOnClickListener {
                onClick(filme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(filmes[position])
    }

    override fun getItemCount(): Int = filmes.size
}
