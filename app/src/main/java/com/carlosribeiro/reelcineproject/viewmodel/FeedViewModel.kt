package com.carlosribeiro.reelcineproject.viewmodel

import android.R.attr.rating
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class FeedViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null

    fun observarRecomendacoesEmTempoReal() {
        listener?.remove() // ✅ evita múltiplos listeners

        listener = db.collection("ratings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("FeedViewModel", "❌ Erro ao escutar ratings: ${error.message}", error)
                    return@addSnapshotListener
                }

                if (snapshots == null) {
                    Log.w("FeedViewModel", "⚠️ Nenhum snapshot retornado")
                    return@addSnapshotListener
                }

                val lista = snapshots.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val titulo = data["movieTitle"] as? String ?: ""  // ✅ Corrigido
                    val comentario = data["comentario"] as? String ?: ""
                    val posterPath = data["posterPath"] as? String ?: ""
                    val avatarUrl = data["avatarUrl"] as? String ?: ""
                    val autor = data["userId"] as? String ?: ""
                    val usuarioNome = data["usuarioNome"] as? String ?: autor

                    // 🕒 Timestamp
                    val timestamp = when (val ts = data["timestamp"]) {
                        is com.google.firebase.Timestamp -> ts.toDate().time
                        is Long -> ts
                        else -> 0L
                    }

                    // ⭐ Campo de nota individual (rating)
                    val rating = when (val rt = data["rating"]) {
                        is Number -> rt.toFloat()
                        is String -> rt.toFloatOrNull() ?: 0f
                        else -> 0f
                    }

                    Log.d("FeedViewModel", "🎯 Rating lido: $rating para $titulo")

                    Recomendacao(
                        titulo = titulo,
                        comentario = comentario,
                        posterPath = posterPath,
                        avatarUrl = avatarUrl,
                        autor = autor,
                        usuarioNome = usuarioNome,
                        timestamp = timestamp,
                        averageRating = 0f,
                        ratingsCount = 0,
                        rating = rating
                    )
                }

                Log.d("FeedViewModel", "📦 Lista final com ${lista.size} recomendações")
                _recomendacoes.value = lista
            }
    }


    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}
