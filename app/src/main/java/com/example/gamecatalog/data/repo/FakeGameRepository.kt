package com.example.gamecatalog.data.repo

import com.example.gamecatalog.data.model.GameDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class FakeGameRepository : GameRepository {
    private val flow = MutableStateFlow(
        listOf(
            GameDTO(UUID.randomUUID().toString(),"Elden Ring","RPG",10,"Exploração e desafio."),
            GameDTO(UUID.randomUUID().toString(),"CS2","FPS",8,"Tático competitivo."),
            GameDTO(UUID.randomUUID().toString(),"FIFA 25","ESPORTES",7,"Futebol anual.")
        )
    )

    override fun streamGames(): Flow<List<GameDTO>> = flow

    override suspend fun addGame(game: GameDTO) {
        val id = game.id.ifBlank { UUID.randomUUID().toString() }
        flow.value = flow.value + game.copy(id = id)
    }

    override suspend fun getById(id: String): GameDTO? =
        flow.value.find { it.id == id }

    override suspend fun updateObservation(id: String, observacao: String) {
        flow.value = flow.value.map { game ->
            if (game.id == id) game.copy(observacao = observacao) else game
        }
    }

    override suspend fun deleteGame(id: String) {
        flow.value = flow.value.filterNot { it.id == id }
    }
}
