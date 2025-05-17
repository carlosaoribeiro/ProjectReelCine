package com.carlosribeiro.reelcineproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.Timestamp
import kotlin.collections.mapNotNull

class FeedViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    private val db = FirebaseFirestore.getInstance()

    fun buscarTodasRecomendacoes() {
        Log.d("FeedViewModel", "🔍 Iniciando busca no Firestore...")

        db.collectionGroup("recomendacoes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val titulo = data["titulo"] as? String ?: ""
                    val comentario = data["comentario"] as? String ?: ""
                    val posterPath = data["posterPath"] as? String ?: ""

                    val timestamp = when (val ts = data["timestamp"]) {
                        is Timestamp -> ts.toDate().time
                        is Long -> ts
                        else -> 0L
                    }

                    Log.d("FeedViewModel", "→ Documento: ${doc.id}, data: $data")

                    Recomendacao(titulo, comentario, posterPath, "", "", "", timestamp)
                }

                _recomendacoes.value = lista
                Log.d("FeedViewModel", "✅ Busca completa. Documentos: ${lista.size}")
            }
            .addOnFailureListener { e ->
                Log.e("FeedViewModel", "❌ Erro ao buscar recomendações: ${e.message}", e)
            }
    }

}
