package com.example.gamecatalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecatalog.data.model.GameDTO
import com.example.gamecatalog.data.repo.FirebaseGameRepository
import com.example.gamecatalog.data.repo.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val games: List<GameDTO> = emptyList(),
    val loading: Boolean = true
)

class GameViewModel(
    private val repo: GameRepository = FirebaseGameRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.streamGames().collect { list ->
                _state.value = UiState(games = list, loading = false)
            }
        }
    }

    fun addGame(
        nome: String,
        genero: String,
        nota: Int,
        descricao: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            repo.addGame(
                GameDTO(
                    nome = nome,
                    genero = genero,
                    nota = nota,
                    descricao = descricao,
                    imageUri = imageUri
                )
            )
        }
    }

    suspend fun getById(id: String): GameDTO? = repo.getById(id)

    fun updateObservation(id: String, observacao: String) {
        viewModelScope.launch {
            repo.updateObservation(id, observacao)
        }
    }

    fun deleteGame(id: String, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)
                repo.deleteGame(id)
                onDone?.invoke()
            } finally {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }
}
