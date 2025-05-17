package com.carlosribeiro.reelcineproject.viewmodel

import androidx.lifecycle.*
import com.carlosribeiro.reelcineproject.api.RetrofitInstance
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.model.toUiModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _filmesTrending = MutableLiveData<List<FilmeUi>>()
    val filmesTrending: LiveData<List<FilmeUi>> = _filmesTrending

    private val _filmesNovidades = MutableLiveData<List<FilmeUi>>()
    val filmesNovidades: LiveData<List<FilmeUi>> = _filmesNovidades

    private val _filmesLancamentos = MutableLiveData<List<FilmeUi>>()
    val filmesLancamentos: LiveData<List<FilmeUi>> = _filmesLancamentos

    private val _filmesTopAvaliados = MutableLiveData<List<FilmeUi>>()
    val filmesTopAvaliados: LiveData<List<FilmeUi>> = _filmesTopAvaliados

    fun carregarFilmesTrending() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesEmAlta()
                _filmesTrending.value = response.results.map { it.toUiModel() }
            } catch (e: Exception) {
                e.printStackTrace()
                _filmesTrending.value = emptyList()
            }
        }
    }

    fun carregarFilmesNovidades() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesRecentes()
                _filmesNovidades.value = response.results.map { it.toUiModel() }
            } catch (e: Exception) {
                e.printStackTrace()
                _filmesNovidades.value = emptyList()
            }
        }
    }

    fun carregarFilmesLancamentos() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesLancamentos()
                _filmesLancamentos.value = response.results.map { it.toUiModel() }
            } catch (e: Exception) {
                e.printStackTrace()
                _filmesLancamentos.value = emptyList()
            }
        }
    }

    fun carregarFilmesTopAvaliados() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesTopAvaliados()
                _filmesTopAvaliados.value = response.results.map { it.toUiModel() }
            } catch (e: Exception) {
                e.printStackTrace()
                _filmesTopAvaliados.value = emptyList()
            }
        }
    }
}
