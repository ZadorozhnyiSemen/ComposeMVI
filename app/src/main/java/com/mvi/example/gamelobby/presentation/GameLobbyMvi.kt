package com.mvi.example.gamelobby.presentation

import com.mvi.example.gamelobby.domain.ObservePlayersUseCase
import com.mvi.example.gamelobby.domain.entity.Player
import com.mvi.mvi.mvi.Mvi

internal class GameLobbyMvi(
    private val observePlayersUseCase: ObservePlayersUseCase = ObservePlayersUseCase()
) : Mvi<GameLobbyState, GameLobbyIntent, GameLobbySingleEvent>() {
    override fun initialState(): GameLobbyState = GameLobbyState.WelcomeScreen

    override fun reduce(
        intent: GameLobbyIntent,
        prevState: GameLobbyState
    ): GameLobbyState = when (intent) {
        is GameLobbyIntent.StartPlayerSearch -> GameLobbyState.GameLobby()
        is GameLobbyIntent.AddPlayerToLobby -> {
            require(prevState is GameLobbyState.GameLobby) { "Exception" }
            if (prevState.lobby.isFull()) {
                prevState
            } else {
                val updatedLobby = prevState.lobby.addPlayer(player = intent.player)
                prevState.copy(
                    lobby = updatedLobby,
                    gameReady = updatedLobby.isFull(),
                )
            }
        }
        is GameLobbyIntent.RemovePlayer -> {
            require(prevState is GameLobbyState.GameLobby) { "Exception" }
            prevState.copy(
                lobby = prevState.lobby.removePlayer(player = intent.player),
                gameReady = false,
            )
        }
        GameLobbyIntent.StartGame -> {
            require(prevState is GameLobbyState.GameLobby)
            prevState.copy(gameStarting = true)
        }
    }

    override suspend fun performSideEffects(
        intent: GameLobbyIntent,
        state: GameLobbyState
    ): GameLobbyIntent? = when (intent) {
        is GameLobbyIntent.AddPlayerToLobby -> null
        is GameLobbyIntent.RemovePlayer -> null
        GameLobbyIntent.StartPlayerSearch -> {
            observeFlow(intent) {
                observePlayersUseCase.invoke(
                    Player(-1, "You", true),
                ).collect {
                    sendIntent(GameLobbyIntent.AddPlayerToLobby(it))
                }
            }
            null
        }
        GameLobbyIntent.StartGame -> {
            triggerSingleEvent(GameLobbySingleEvent.NotifyGameStarting)
            null
        }
    }
}