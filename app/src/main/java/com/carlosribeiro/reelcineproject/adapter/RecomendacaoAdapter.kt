package com.carlosribeiro.reelcineproject.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ItemRecomendacaoBinding
import com.carlosribeiro.reelcineproject.model.Recomendacao
import java.text.SimpleDateFormat
import java.util.*

class RecomendacaoAdapter : ListAdapter<Recomendacao, RecomendacaoAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recomendacao>() {
            override fun areItemsTheSame(oldItem: Recomendacao, newItem: Recomendacao): Boolean {
                return oldItem.titulo == newItem.titulo && oldItem.usuarioNome == newItem.usuarioNome
            }

            override fun areContentsTheSame(oldItem: Recomendacao, newItem: Recomendacao): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemRecomendacaoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recomendacao: Recomendacao) {
            try {
                binding.textTitulo.text = recomendacao.titulo
                binding.textComentario.text = recomendacao.comentario
                binding.textAutor.text = recomendacao.usuarioNome

                // ✅ Corrigido: usa a string formatada
                val dataFormatada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataTexto = dataFormatada.format(Date(recomendacao.timestamp))
                binding.textData.text = dataTexto

                // ✅ Glide para carregar imagem
                Glide.with(binding.root.context)
                    .load(recomendacao.posterPath)
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.imagePoster)

            } catch (e: Exception) {
                Log.e("AdapterErro", "Erro ao bindar item: ${e.message}", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecomendacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


