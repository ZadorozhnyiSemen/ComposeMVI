package com.mvi.example.gamelobby.domain.entity

data class Player(
    val id: Int,
    val name: String,
    val isHost: Boolean,
)
