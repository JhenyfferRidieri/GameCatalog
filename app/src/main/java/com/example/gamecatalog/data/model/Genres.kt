package com.example.gamecatalog.data.model

open class Jogo(
    open val id: String = "",
    open val nome: String = "",
    open val nota: Int = 0
) {
    open fun descricaoGenero(): String = "Jogo"
}

class RPG(
    override val id: String = "",
    override val nome: String = "",
    override val nota: Int = 0
) : Jogo(id, nome, nota) {
    override fun descricaoGenero() = "RPG — progressão e narrativa."
}

class FPS(
    override val id: String = "",
    override val nome: String = "",
    override val nota: Int = 0
) : Jogo(id, nome, nota) {
    override fun descricaoGenero() = "FPS — ação em primeira pessoa."
}

class Esporte(
    override val id: String = "",
    override val nome: String = "",
    override val nota: Int = 0,
    val tipo: String = "Futebol"
) : Jogo(id, nome, nota) {
    override fun descricaoGenero() = "Esporte — simulação de ${tipo.lowercase()}."
}
