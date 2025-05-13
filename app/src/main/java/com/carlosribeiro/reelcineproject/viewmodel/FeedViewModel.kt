package com.carlosribeiro.reelcineproject.viewmodel

import androidx.lifecycle.*
import com.carlosribeiro.reelcineproject.api.RetrofitClient
import com.carlosribeiro.reelcineproject.model.Filme
import com.carlosribeiro.reelcineproject.model.Recomendacao
import com.carlosribeiro.reelcineproject.repository.RecomendacaoRepository
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {

    private val _filmes = MutableLiveData<List<Filme>>()
    val filmes: LiveData<List<Filme>> = _filmes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchMovies() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.instance.getPopularMovies()
                if (response.isSuccessful) {
                    _filmes.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = "Erro API: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
