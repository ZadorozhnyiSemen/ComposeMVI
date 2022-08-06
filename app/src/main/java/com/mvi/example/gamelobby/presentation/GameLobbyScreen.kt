package com.mvi.example.gamelobby.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvi.example.gamelobby.domain.entity.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun GameLobbyScreenHolder(
    mvi: GameLobbyMvi = viewModel()
) {
    val scaffoldState = rememberScaffoldState()

    val state by mvi.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mvi.singleEvent.onEach {
            when (it) {
                GameLobbySingleEvent.NotifyGameStarting -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Game starting in 3",
                        duration = SnackbarDuration.Short
                    )
                    delay(2)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Game starting in 2",
                    )
                    delay(2)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Game starting in 1",
                    )
                }
            }
        }.collect()
    }

    GameLobbyScreen(
        state = state,
        scaffoldState = scaffoldState,
        onCreateLobby = { mvi.sendIntent(GameLobbyIntent.StartPlayerSearch) },
        onRemovePlayer = { mvi.sendIntent(GameLobbyIntent.RemovePlayer(it)) },
        onStartGame = { mvi.sendIntent(GameLobbyIntent.StartGame) }
    )
}

@Composable
private fun GameLobbyScreen(
    state: GameLobbyState,
    scaffoldState: ScaffoldState,
    onCreateLobby: () -> Unit,
    onStartGame: () -> Unit,
    onRemovePlayer: (Player) -> Unit,
) {
    Scaffold(
        scaffoldState = scaffoldState
    ) { padding ->
        when (state) {
            is GameLobbyState.GameLobby -> GameLobbyComponent(
                modifier = Modifier.padding(padding),
                state = state,
                onRemovePlayer = onRemovePlayer,
                onStartGame = onStartGame,
            )
            GameLobbyState.WelcomeScreen -> WelcomeScreenComponent(
                onCreateLobby = onCreateLobby
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameLobbyComponent(
    modifier: Modifier = Modifier,
    state: GameLobbyState.GameLobby,
    onRemovePlayer: (Player) -> Unit,
    onStartGame: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                items(items = state.lobby.players) {
                    PlayerItem(
                        player = it,
                        gameStarted = state.gameStarting,
                        onRemovePlayer = { onRemovePlayer(it) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Crossfade(targetState = state.gameReady) { gameReady ->
                if (gameReady) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
                        onClick = onStartGame,
                        enabled = !state.gameStarting,
                    ) {
                        Text(text = "Start game", textAlign = TextAlign.Center)
                    }
                } else {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Waiting for the players...",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun PlayerItem(
    player: Player,
    onRemovePlayer: () -> Unit,
    gameStarted: Boolean,
) {
    Row(Modifier.background(if (player.isHost) Color.Gray else Color.White)) {
        Text(
            modifier = Modifier.weight(1f),
            text = player.name,
            style = MaterialTheme.typography.body2,
        )
        if (!player.isHost) {
            Button(
                onClick = onRemovePlayer,
                enabled = !gameStarted,
            ) {
                Text(text = "Kick")
            }
        }
    }
}

@Composable
private fun WelcomeScreenComponent(
    onCreateLobby: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        OutlinedButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = onCreateLobby,
        ) {
            Text(text = "Create lobby", style = MaterialTheme.typography.h2)
        }
    }
}
