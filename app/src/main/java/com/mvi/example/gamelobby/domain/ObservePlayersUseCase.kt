package com.mvi.example.gamelobby.domain

import com.mvi.example.gamelobby.data.PlayersRepository
import com.mvi.example.gamelobby.domain.entity.Player
import kotlinx.coroutines.flow.Flow

internal class ObservePlayersUseCase {

    suspend operator fun invoke(you: Player): Flow<Player> {
        return PlayersRepository.getPlayerFlow(you)
    }
}