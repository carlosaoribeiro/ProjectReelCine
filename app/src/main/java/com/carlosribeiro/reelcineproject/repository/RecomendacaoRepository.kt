package com.carlosribeiro.reelcineproject.repository

import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecomendacaoRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getTodasRecomendacoes(): List<Recomendacao> {
        val lista = mutableListOf<Recomendacao>()

        val usuariosSnapshot = db.collection("usuarios").get().await()

        for (usuario in usuariosSnapshot) {
            val nome = usuario.getString("nome") ?: "Desconhecido"
            val recomendacoesSnapshot = db.collection("usuarios")
                .document(usuario.id)
                .collection("recomendacoes")
                .get().await()

            for (recomendacaoDoc in recomendacoesSnapshot) {
                val recomendacao = recomendacaoDoc.toObject(Recomendacao::class.java)
                lista.add(recomendacao.copy(usuarioNome = nome))
            }
        }

        return lista.sortedByDescending { it.timestamp }
    }
}
