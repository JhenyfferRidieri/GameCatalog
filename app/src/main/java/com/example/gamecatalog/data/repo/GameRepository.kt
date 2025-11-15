package com.example.gamecatalog.data.repo

import com.example.gamecatalog.data.model.GameDTO
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun streamGames(): Flow<List<GameDTO>>
    suspend fun addGame(game: GameDTO)
    suspend fun getById(id: String): GameDTO?
    suspend fun updateObservation(id: String, observacao: String)
    suspend fun deleteGame(id: String)
}