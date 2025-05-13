package com.carlosribeiro.reelcineproject.ui.recomendacao

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ItemFilmeBinding
import com.carlosribeiro.reelcineproject.model.FilmeUi

class FilmeAdapter(
    private val onFilmeClick: (FilmeUi) -> Unit
) : ListAdapter<FilmeUi, FilmeAdapter.FilmeViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilmeUi>() {
            override fun areItemsTheSame(oldItem: FilmeUi, newItem: FilmeUi) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FilmeUi, newItem: FilmeUi) =
                oldItem == newItem
        }
    }

    inner class FilmeViewHolder(private val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filme: FilmeUi) {
            binding.textTitulo.text = filme.titulo
            binding.textDescricao.text = filme.descricao
            binding.textAno.text = filme.ano

            Glide.with(binding.root.context)
                .load(filme.imagemUrl)
                .into(binding.imagePoster)

            binding.root.setOnClickListener {
                onFilmeClick(filme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
