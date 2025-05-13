package com.carlosribeiro.reelcineproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp


class RecomendacoesViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    fun buscarTodasRecomendacoes() {
        viewModelScope.launch {
            val lista = mutableListOf<Recomendacao>()
            val db = FirebaseFirestore.getInstance()
            val usuariosSnapshot = db.collection("usuarios").get().await()
            Log.d("ViewModel", "Usuários encontrados: ${usuariosSnapshot.size()}")


            for (usuario in usuariosSnapshot) {
                val nome = usuario.getString("nome") ?: "Anônimo"
                val recomendacoesSnapshot = db.collection("usuarios")
                    .document(usuario.id)
                    .collection("recomendacoes")
                    .get()
                    .await()

                Log.d("ViewModel", "Verificando usuário: ${usuario.id}")

                for (recomendacaoDoc in recomendacoesSnapshot) {
                    val data = recomendacaoDoc.data
                    val recomendacao = Recomendacao(
                        titulo = data["titulo"] as? String ?: "",
                        comentario = data["comentario"] as? String ?: "",
                        posterPath = data["posterPath"] as? String ?: "",
                        avatarUrl = data["avatarUrl"] as? String ?: "",
                        autor = nome,
                        usuarioNome = nome,
                        timestamp = (data["timestamp"] as? Timestamp)?.toDate()?.time ?: 0L // ✅ Aqui
                    )
                    lista.add(recomendacao)
                }
            }

            // ✅ Ordenar por timestamp decrescente (mais recentes primeiro)
            _recomendacoes.value = lista.sortedByDescending { it.timestamp }

            Log.d("ViewModel", "Total recomendacoes: ${lista.size}")
        }
    }
}
