import androidx.lifecycle.*
import com.carlosribeiro.reelcineproject.api.RetrofitInstance
import com.carlosribeiro.reelcineproject.model.FilmeUi
import com.carlosribeiro.reelcineproject.model.mapper.toUiModel
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
                if (response.isSuccessful) {
                    _filmesTrending.value = response.body()?.results?.map { it.toUiModel() }
                }
            } catch (_: Exception) {}
        }
    }

    fun carregarFilmesNovidades() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesRecentes()
                if (response.isSuccessful) {
                    _filmesNovidades.value = response.body()?.results?.map { it.toUiModel() }
                }
            } catch (_: Exception) {}
        }
    }

    fun carregarFilmesLancamentos() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesLancamentos()
                if (response.isSuccessful) {
                    _filmesLancamentos.value = response.body()?.results?.map { it.toUiModel() }
                }
            } catch (_: Exception) {}
        }
    }

    fun carregarFilmesTopAvaliados() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.buscarFilmesTopAvaliados()
                if (response.isSuccessful) {
                    _filmesTopAvaliados.value = response.body()?.results?.map { it.toUiModel() }
                }
            } catch (_: Exception) {}
        }
    }
}
