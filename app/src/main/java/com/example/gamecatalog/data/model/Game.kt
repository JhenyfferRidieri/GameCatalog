package com.example.gamecatalog.data.model

data class GameDTO(
    val id: String = "",
    val nome: String = "",
    val genero: String = "",
    val nota: Int = 0,
    val descricao: String = "",
    val imageUri: String? = null,
    val observacao: String = ""
)