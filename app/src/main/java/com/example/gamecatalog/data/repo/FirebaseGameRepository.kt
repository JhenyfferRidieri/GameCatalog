package com.example.gamecatalog.data.repo

import com.example.gamecatalog.data.model.GameDTO
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseGameRepository : GameRepository {

    private val db = FirebaseFirestore.getInstance()
    private val gamesCol = db.collection("games")

    override fun streamGames(): Flow<List<GameDTO>> = callbackFlow {
        val reg = gamesCol
            .orderBy("nome")
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val list = snap?.documents
                    ?.map { it.toGameDTO() }
                    ?: emptyList()

                trySend(list).isSuccess
            }

        awaitClose { reg.remove() }
    }

    override suspend fun addGame(game: GameDTO) {
        val id = if (game.id.isBlank()) gamesCol.document().id else game.id

        val data = hashMapOf(
            "id" to id,
            "nome" to game.nome,
            "genero" to game.genero,
            "nota" to game.nota,
            "descricao" to game.descricao,
            "imageUri" to game.imageUri,
            "observacao" to game.observacao
        )

        gamesCol.document(id).set(data).await()
    }

    override suspend fun getById(id: String): GameDTO? =
        gamesCol.document(id).get().await().let { snap ->
            if (snap.exists()) snap.toGameDTO() else null
        }

    override suspend fun updateObservation(id: String, observacao: String) {
        gamesCol.document(id).update("observacao", observacao).await()
    }

    override suspend fun deleteGame(id: String) {
        gamesCol.document(id).delete().await()
    }

    private fun DocumentSnapshot.toGameDTO(): GameDTO = GameDTO(
        id = getString("id") ?: id ?: "",
        nome = getString("nome") ?: "",
        genero = getString("genero") ?: "",
        nota = (getLong("nota") ?: 0L).toInt(),
        descricao = getString("descricao") ?: "",
        imageUri = getString("imageUri"),
        // NOVO: leitura da observação
        observacao = getString("observacao") ?: ""
    )
}
