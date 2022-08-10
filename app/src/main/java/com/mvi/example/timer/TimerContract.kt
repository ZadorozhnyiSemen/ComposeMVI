package com.mvi.example.timer

import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State

internal data class TimerState(
    val timeLeft: Int = 0,
    val completed: Boolean = false,
) : State

internal sealed interface TimerIntent : Intent {
    data class StartTimer(val from: Int) : TimerIntent
    data class UpdateTimer(val newTime: Int) : TimerIntent
    object TimeIsUp : TimerIntent
}

internal sealed interface TimerSingleEvent : SingleEvent {
    object ShowNotification : TimerSingleEvent
}