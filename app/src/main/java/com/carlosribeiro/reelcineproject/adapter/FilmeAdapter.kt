package com.carlosribeiro.reelcineproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.model.Filme

class FilmeAdapter(private val listaFilmes: List<Filme>) :
    RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filme, parent, false)
        return FilmeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(listaFilmes[position])
    }

    override fun getItemCount(): Int = listaFilmes.size

    class FilmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo: TextView = itemView.findViewById(R.id.textTitulo)
        private val imagem: ImageView = itemView.findViewById(R.id.imageFilme)

        fun bind(filme: Filme) {
            titulo.text = filme.title
            imagem.load("https://image.tmdb.org/t/p/w500${filme.poster_path}")
        }
    }
}
