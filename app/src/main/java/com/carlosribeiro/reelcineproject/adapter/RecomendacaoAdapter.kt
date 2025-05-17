package com.carlosribeiro.reelcineproject.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.R
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
                // Título, comentário e nome do autor
                binding.textTitulo.text = recomendacao.titulo
                binding.textComentario.text = recomendacao.comentario
                binding.textAutor.text = "Postado por: ${recomendacao.usuarioNome}"

                // Formatar data
                val dataFormatada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataTexto = dataFormatada.format(Date(recomendacao.timestamp))
                binding.textData.text = dataTexto

                // Imagem do filme
                Glide.with(binding.root.context)
                    .load(recomendacao.posterPath)
                    .placeholder(android.R.color.darker_gray)
                    .into(binding.imagePoster)

                // ✅ Avatar com fallback se URL estiver vazia ou inválida
                Glide.with(binding.root.context)
                    .load(
                        if (recomendacao.avatarUrl.isNullOrBlank())
                            R.drawable.avatar_placeholder
                        else
                            recomendacao.avatarUrl
                    )
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .circleCrop()
                    .into(binding.imageAvatar)

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
