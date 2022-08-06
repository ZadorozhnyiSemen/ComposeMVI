package com.mvi.example.gamelobby.presentation

import com.mvi.example.gamelobby.domain.entity.Lobby
import com.mvi.example.gamelobby.domain.entity.Player
import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State

sealed interface GameLobbyState : State {
    object WelcomeScreen : GameLobbyState
    data class GameLobby(
        val lobby: Lobby = Lobby(),
        val gameReady: Boolean = false,
        val gameStarting: Boolean = false,
    ) : GameLobbyState
}

sealed interface GameLobbyIntent : Intent {
    object StartPlayerSearch : GameLobbyIntent
    data class AddPlayerToLobby(val player: Player) : GameLobbyIntent
    data class RemovePlayer(val player: Player) : GameLobbyIntent
    object StartGame : GameLobbyIntent
}

sealed interface GameLobbySingleEvent : SingleEvent {
    object NotifyGameStarting : GameLobbySingleEvent
}