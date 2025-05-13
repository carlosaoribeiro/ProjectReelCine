package com.carlosribeiro.reelcineproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosribeiro.reelcineproject.api.RetrofitInstance
import com.carlosribeiro.reelcineproject.model.FilmeResponse
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.model.mapper.toUiModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BuscarFilmeViewModel : ViewModel() {

    private val _resultado = MutableLiveData<List<FilmeUi>>()
    val resultado: LiveData<List<FilmeUi>> = _resultado

    fun buscarFilmes(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmes(query)
                if (response.isSuccessful) {
                    val filmesResponse = response.body()
                    _resultado.value = filmesResponse?.results?.map { it.toUiModel() } ?: emptyList()
                } else {
                    _resultado.value = emptyList()
                }
            } catch (e: Exception) {
                _resultado.value = emptyList()

                Log.e("Buscar FIlme", "Error", e);
            }
        }
    }
}
