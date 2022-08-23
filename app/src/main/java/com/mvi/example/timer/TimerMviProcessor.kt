package com.mvi.example.timer

import com.mvi.mvi.mvi.MviProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

private const val TimerTaskId = "Timer task"

internal class TimerMviProcessor constructor() : MviProcessor<
	TimerState,
	TimerIntent,
	TimerSingleEvent
	>() {

	init {
		sendIntent(TimerIntent.StartTimer(30))
	}

	override fun initialState(): TimerState = TimerState()

	override val reducer: Reducer<TimerState, TimerIntent>
		get() = TimerScreenReducer()

	override suspend fun handleIntent(
		intent: TimerIntent,
		state: TimerState
	): TimerIntent?
		= when (intent) {
		is TimerIntent.StartTimer -> handleStartTimer(intent)
		is TimerIntent.TimeIsUp -> handleTimeIsUp()
		is TimerIntent.UpdateTimer -> null
	}
}

internal fun TimerMviProcessor.handleStartTimer(
	intent: TimerIntent.StartTimer,
): TimerIntent? {
	observeFlow(TimerTaskId) {
		observeSecondTick(intent.from)
			.onCompletion { sendIntent(TimerIntent.TimeIsUp) }
			.collect { timerValue ->
				sendIntent(TimerIntent.UpdateTimer(timerValue))
			}
	}
	return null
}

internal fun TimerMviProcessor.handleTimeIsUp(): TimerIntent? {
	triggerSingleEvent(TimerSingleEvent.ShowNotification)
	return null
}

suspend fun observeSecondTick(from: Int): Flow<Int> = flow {
	var timerValue = from
	emit(timerValue)
	while (timerValue > 0) {
		delay(1000L)
		timerValue -= 1
		emit(timerValue)
	}
}.flowOn(Dispatchers.IO)