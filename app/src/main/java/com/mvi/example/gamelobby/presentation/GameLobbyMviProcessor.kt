package com.mvi.example.gamelobby.presentation

import com.mvi.example.gamelobby.domain.ObservePlayersUseCase
import com.mvi.example.gamelobby.domain.entity.Player
import com.mvi.mvi.mvi.MviProcessor

private const val GameLobbyTaskId = "task_1"

internal class GameLobbyMviProcessor(
    private val observePlayersUseCase: ObservePlayersUseCase = ObservePlayersUseCase()
) : MviProcessor<GameLobbyState, GameLobbyIntent, GameLobbySingleEvent>() {

    override val reducer: Reducer<GameLobbyState, GameLobbyIntent>
        get() = GameLobbyReducer()

    override fun initialState(): GameLobbyState = GameLobbyState.WelcomeScreen

    override suspend fun handleIntent(
        intent: GameLobbyIntent,
        state: GameLobbyState
    ): GameLobbyIntent? = when (intent) {
        is GameLobbyIntent.AddPlayerToLobby -> null
        is GameLobbyIntent.RemovePlayer -> null
        GameLobbyIntent.StartPlayerSearch -> {
            observeFlow(GameLobbyTaskId) {
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

internal class GameLobbyReducer : MviProcessor.Reducer<GameLobbyState, GameLobbyIntent> {

    override fun reduce(
        state: GameLobbyState,
        intent: GameLobbyIntent
    ): GameLobbyState = when (intent) {
        is GameLobbyIntent.StartPlayerSearch -> GameLobbyState.GameLobby()
        is GameLobbyIntent.AddPlayerToLobby -> {
            require(state is GameLobbyState.GameLobby) { "Exception" }
            if (state.lobby.isFull()) {
                state
            } else {
                val updatedLobby = state.lobby.addPlayer(player = intent.player)
                state.copy(
                    lobby = updatedLobby,
                    gameReady = updatedLobby.isFull(),
                )
            }
        }
        is GameLobbyIntent.RemovePlayer -> {
            require(state is GameLobbyState.GameLobby) { "Exception" }
            state.copy(
                lobby = state.lobby.removePlayer(player = intent.player),
                gameReady = false,
            )
        }
        GameLobbyIntent.StartGame -> {
            require(state is GameLobbyState.GameLobby)
            state.copy(gameStarting = true)
        }
    }
}