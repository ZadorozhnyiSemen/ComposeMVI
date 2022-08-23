package com.mvi.mvi.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class MviProcessor<S : State, I : Intent, E : SingleEvent> : ViewModel() {


	private val initialState: S by lazy { initialState() }

	protected abstract fun initialState(): S

	private val _viewState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
	val viewState: StateFlow<S> by lazy { _viewState }

	private val _intent: MutableSharedFlow<I> = MutableSharedFlow()

	private val _singleEvent: MutableSharedFlow<E> = MutableSharedFlow()
	val singleEvent: Flow<E> = _singleEvent

	protected abstract val reducer: Reducer<S, I>

	private val longRunningJobs: HashMap<String, Job> = hashMapOf()

	init {
		subscribeToIntents()
	}


	fun sendIntent(intent: I) {
		viewModelScope.launch {
			_intent.emit(intent)
		}
	}

	protected abstract suspend fun handleIntent(intent: I, state: S): I?

	fun triggerSingleEvent(singleEvent: E) {
		viewModelScope.launch { _singleEvent.emit(singleEvent) }
	}

	private fun setState(newState: S.() -> S) {
		_viewState.value = viewState.value.newState()
	}

	private fun reduceInternal(prevState: S, intent: I) {
		val newState = reducer.reduce(prevState, intent)
		setState { newState }
	}

	fun observeFlow(
		taskId: String,
		isUnique: Boolean = true,
		taskStartedByIntent: suspend () -> Unit
	) {
		when {
			taskId in longRunningJobs.keys &&
				!(longRunningJobs[taskId]?.isCompleted ?: true) &&
				isUnique -> {
				Log.d("CoroutinesViewModel", "Job for intent already working.. Skip execution")
				return
			}
			!isUnique -> {
				if (taskId in longRunningJobs.keys) longRunningJobs[taskId]?.cancel()
			}
		}
		val task = viewModelScope.launch {
			taskStartedByIntent()
		}
		longRunningJobs[taskId] = task
	}

	fun cancelFlow(taskId: String) {
		if (taskId in longRunningJobs.keys && longRunningJobs[taskId]?.isActive == true) {
			longRunningJobs[taskId]?.cancel()
		}
	}

	private fun subscribeToIntents() {
		viewModelScope.launch {
			_intent.collect {
				reduceInternal(_viewState.value, it)
				launch {
					handleIntent(it, _viewState.value)?.let { newIntent -> sendIntent(newIntent) }
				}
			}
		}
	}

	interface Reducer<S, I> {
		fun reduce(state: S, intent: I): S
	}
}