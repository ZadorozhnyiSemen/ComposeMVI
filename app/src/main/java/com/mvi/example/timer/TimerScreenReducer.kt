package com.mvi.example.timer

import com.mvi.mvi.mvi.MviProcessor

internal class TimerScreenReducer : MviProcessor.Reducer<TimerState, TimerIntent> {

    override fun reduce(
        state: TimerState,
        intent: TimerIntent
    ): TimerState = when (intent) {
        is TimerIntent.StartTimer -> state
        TimerIntent.TimeIsUp -> {
            state.copy(completed = true)
        }
        is TimerIntent.UpdateTimer -> {
            state.copy(timeLeft = intent.newTime)
        }
    }
}