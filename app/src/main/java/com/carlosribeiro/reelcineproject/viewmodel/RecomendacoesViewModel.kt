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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query


class RecomendacoesViewModel : ViewModel() {

    private val _recomendacoes = MutableLiveData<List<Recomendacao>>()
    val recomendacoes: LiveData<List<Recomendacao>> = _recomendacoes

    fun buscarTodasRecomendacoes() {
        Log.d("RecomendacoesVM", "Iniciando busca por recomendações...")

        val user = FirebaseAuth.getInstance().currentUser
        Log.d("RecomendacoesVM", "UID atual: ${user?.uid}")

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
                Log.d("RecomendacoesVM", "Recomendações carregadas: ${result.size()}")
                val lista = result.mapNotNull { doc ->
                    try {
                        doc.toObject(Recomendacao::class.java)
                    } catch (e: Exception) {
                        Log.e("RecomendacoesVM", "Erro ao converter documento: ${e.message}")
                        null
                    }
                }
                _recomendacoes.value = lista
            }
            .addOnFailureListener { e ->
                Log.e("RecomendacoesVM", "Erro ao buscar recomendações: ${e.message}")
            }
    }

}





