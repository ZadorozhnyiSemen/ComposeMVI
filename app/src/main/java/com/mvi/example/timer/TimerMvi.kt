package com.mvi.example.timer

import com.mvi.mvi.mvi.Mvi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

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

internal class TimerMvi constructor() : Mvi<
	TimerState,
	TimerIntent,
	TimerSingleEvent
	>() {

	init {
		sendIntent(TimerIntent.StartTimer(30))
	}

	override fun initialState(): TimerState = TimerState()

	override fun reduce(
		intent: TimerIntent,
		prevState: TimerState
	): TimerState = timerScreenReducer(intent, prevState)

//	override suspend fun performSideEffects(
//		intent: TimerIntent,
//		state: TimerState
//	): TimerIntent?
//	= when (intent) {
//		is TimerIntent.StartTimer -> {
//			observeFlow(intent) {
//				observeSecondTick(intent.from)
//					.onCompletion { sendIntent(TimerIntent.TimeIsUp) }
//					.collect { timerValue ->
//						sendIntent(TimerIntent.UpdateTimer(timerValue))
//					}
//			}
//			null
//		}
//		TimerIntent.TimeIsUp -> {
//			triggerSingleEvent(TimerSingleEvent.ShowNotification)
//			null
//		}
//		is TimerIntent.UpdateTimer -> null
//	}

	override suspend fun performSideEffects(
		intent: TimerIntent,
		state: TimerState
	): TimerIntent?
		= when (intent) {
		is TimerIntent.StartTimer -> handleStartTimer(intent, state)
		is TimerIntent.TimeIsUp -> handleTimeIsUp(intent, state)
		is TimerIntent.UpdateTimer -> null
	}
}

internal fun TimerMvi.handleStartTimer(
	intent: TimerIntent.StartTimer,
	state: TimerState,
): TimerIntent? {
	observeFlow(intent) {
		observeSecondTick(intent.from)
			.onCompletion { sendIntent(TimerIntent.TimeIsUp) }
			.collect { timerValue ->
				sendIntent(TimerIntent.UpdateTimer(timerValue))
			}
	}
	return null
}

internal fun TimerMvi.handleTimeIsUp(
	intent: TimerIntent.TimeIsUp,
	state: TimerState,
): TimerIntent? {
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