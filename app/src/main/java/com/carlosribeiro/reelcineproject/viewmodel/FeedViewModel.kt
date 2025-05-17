package com.carlosribeiro.reelcineproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    private val db = FirebaseFirestore.getInstance()

    fun observarRecomendacoesEmTempoReal() {
        db.collectionGroup("recomendacoes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("FeedViewModel", "❌ Erro ao escutar recomendações: \${error.message}", error)
                    return@addSnapshotListener
                }

                val lista = snapshots?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val titulo = data["titulo"] as? String ?: ""
                    val comentario = data["comentario"] as? String ?: ""
                    val posterPath = data["posterPath"] as? String ?: ""
                    val avatarUrl = data["avatarUrl"] as? String ?: ""
                    val autor = data["autor"] as? String ?: ""
                    val usuarioNome = data["usuarioNome"] as? String ?: ""

                    val timestamp = when (val ts = data["timestamp"]) {
                        is Timestamp -> ts.toDate().time
                        is Long -> ts
                        else -> 0L
                    }

                    Recomendacao(
                        titulo = titulo,
                        comentario = comentario,
                        posterPath = posterPath,
                        avatarUrl = avatarUrl,
                        autor = autor,
                        usuarioNome = usuarioNome,
                        timestamp = timestamp
                    )
                }

                _recomendacoes.value = lista!!
                Log.d("FeedViewModel", "📡 Recomendações em tempo real atualizadas: \${lista?.size}")
            }
    }
}
