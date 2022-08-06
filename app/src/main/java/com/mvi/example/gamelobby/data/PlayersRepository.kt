package com.mvi.example.gamelobby.data

import com.mvi.example.gamelobby.domain.entity.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PlayersRepository {

    suspend fun getPlayerFlow(you: Player): Flow<Player> = flow {
        emit(you)
        while (true) {
            delay((1000..3000).random().toLong())
            emit(generatePlayer())
        }
    }

    private fun generatePlayer(): Player {
        val randomId = (0..1000).random()
        return Player(
            randomId,
            "Player_$randomId",
            false,
        )
    }
}