package com.mvi.example.gamelobby.domain.entity

data class Lobby(
    val players: List<Player> = mutableListOf(),
    val lobbySize: Int = 10,
) {
    fun isFull() = players.size >= lobbySize

    fun addPlayer(player: Player): Lobby = this.copy(
        players = players + player
    )

    fun removePlayer(player: Player): Lobby = this.copy(
        players = players - player
    )
}
