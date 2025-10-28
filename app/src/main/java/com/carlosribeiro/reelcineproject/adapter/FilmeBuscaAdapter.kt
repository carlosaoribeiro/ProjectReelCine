package com.carlosribeiro.reelcineproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ItemFilmeBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi

class FilmeAdapter(
    private var filmes: List<FilmeUi> = emptyList(),
    private val onSelecionarClick: (FilmeUi) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    inner class FilmeViewHolder(val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = filmes[position]
        holder.binding.apply {
            textTitulo.text = filme.titulo
            textAno.text = filme.ano
            textDescricao.text = filme.descricao ?: "Descrição indisponível"

            val posterUrl = filme.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            } ?: ""

            Glide.with(root.context)
                .load(posterUrl)
                .placeholder(com.carlosribeiro.reelcineproject.R.drawable.bg_image_placeholder)
                .error(com.carlosribeiro.reelcineproject.R.drawable.bg_image_placeholder)
                .into(imagePoster)

            btnSelecionar.setOnClickListener {
                onSelecionarClick(filme)
            }
        }
    }

    override fun getItemCount() = filmes.size

    fun submitList(novaLista: List<FilmeUi>?) {
        filmes = novaLista ?: emptyList()
        notifyDataSetChanged()
    }
}
