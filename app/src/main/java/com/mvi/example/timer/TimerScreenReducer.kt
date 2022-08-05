package com.mvi.example.timer

internal fun timerScreenReducer(
    intent: TimerIntent,
    prevState: TimerState
) = when (intent) {
    is TimerIntent.StartTimer -> prevState
    TimerIntent.TimeIsUp -> prevState.copy(completed = true)
    is TimerIntent.UpdateTimer -> {
        prevState.copy(timeLeft = intent.newTime)
    }
}