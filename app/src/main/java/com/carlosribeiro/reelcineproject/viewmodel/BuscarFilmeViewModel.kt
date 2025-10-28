package com.carlosribeiro.reelcineproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.model.mapper.toUiModel

import com.carlosribeiro.reelcineproject.network.response.FilmeResponse
import com.carlosribeiro.reelcineproject.api.RetrofitInstance
import kotlinx.coroutines.launch

class BuscarFilmeViewModel : ViewModel() {

    private val _resultado = MutableLiveData<List<FilmeUi>>()
    val resultado: LiveData<List<FilmeUi>> get() = _resultado

    fun buscarFilmes(query: String) {
        viewModelScope.launch {
            try {
                val response: FilmeResponse = RetrofitInstance.api.buscarFilmes(query)
                val filmesUi = response.results?.map { it.toUiModel() } ?: emptyList()
                _resultado.postValue(filmesUi)
            } catch (e: Exception) {
                e.printStackTrace()
                _resultado.postValue(emptyList())
            }
        }
    }
}
