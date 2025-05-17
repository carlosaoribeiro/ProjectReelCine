package com.carlosribeiro.reelcineproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class RecomendacoesViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    fun buscarTodasRecomendacoes() {
        Log.d("RecomendacoesVM", "Iniciando busca por recomendações...")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        if (uid == null) {
            Log.e("RecomendacoesVM", "UID do usuário é nulo.")
            return
        }

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(uid)
            .collection("recomendacoes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        doc.toObject(Recomendacao::class.java)
                    } catch (e: Exception) {
                        Log.e("RecomendacoesVM", "Erro ao converter documento: ${e.message}")
                        null
                    }
                }
                _recomendacoes.value = lista
                Log.d("RecomendacoesVM", "Recomendações carregadas: ${lista.size}")
            }
            .addOnFailureListener { e ->
                Log.e("RecomendacoesVM", "Erro ao buscar recomendações: ${e.message}")
            }
    }

    fun adicionarRecomendacao(recomendacao: Recomendacao) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        if (uid == null) {
            Log.e("RecomendacoesVM", "UID do usuário é nulo.")
            return
        }

        val recomendacaoMap = hashMapOf(
            "titulo" to recomendacao.titulo,
            "comentario" to recomendacao.comentario,
            "posterPath" to recomendacao.posterPath,
            "timestamp" to FieldValue.serverTimestamp(),
            "usuarioNome" to recomendacao.usuarioNome,
            "autor" to recomendacao.autor,
            "avatarUrl" to recomendacao.avatarUrl
        )

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(uid)
            .collection("recomendacoes")
            .add(recomendacaoMap)
            .addOnSuccessListener {
                Log.d("RecomendacoesVM", "✅ Recomendação salva com sucesso.")
                buscarTodasRecomendacoes() // 🔄 Atualiza o feed imediatamente
            }
            .addOnFailureListener { e ->
                Log.e("RecomendacoesVM", "❌ Erro ao salvar recomendação: ${e.message}")
            }
    }
}
