package com.mvi.mvi.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class Mvi<S : State, I : Intent, E : SingleEvent> : ViewModel() {


	private val initialState: S by lazy { initialState() }

	protected abstract fun initialState(): S

	private val _viewState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
	val viewState: StateFlow<S> by lazy { _viewState }

	private val _intent: MutableSharedFlow<I> = MutableSharedFlow()

	private val _singleEvent: MutableSharedFlow<E> = MutableSharedFlow()
	val singleEvent: Flow<E> = _singleEvent

	private val longRunningJobs: HashMap<I, Job> = hashMapOf()

	init {
		subscribeToIntents()
	}


	fun sendIntent(intent: I) {
		viewModelScope.launch {
			_intent.emit(intent)
		}
	}

	protected abstract fun reduce(intent: I, prevState: S): S

	protected abstract suspend fun performSideEffects(intent: I, state: S): I?

	protected fun triggerSingleEvent(singleEvent: E) {
		viewModelScope.launch { _singleEvent.emit(singleEvent) }
	}

	private fun setState(newState: S.() -> S) {
		_viewState.value = viewState.value.newState()
	}

	private fun reduceInternal(intent: I, prevState: S) {
		val newState = reduce(intent, prevState)
		setState { newState }
	}

	protected fun observeFlow(
		taskTriggerIntent: I,
		isUnique: Boolean = true,
		taskStartedByIntent: suspend () -> Unit
	) {
		when {
			taskTriggerIntent in longRunningJobs.keys &&
				!(longRunningJobs[taskTriggerIntent]?.isCompleted ?: true) &&
				isUnique -> {
				Log.d("CoroutinesViewModel", "Job for intent already working.. Skip execution")
				return
			}
			!isUnique -> {
				if (taskTriggerIntent in longRunningJobs.keys) longRunningJobs[taskTriggerIntent]?.cancel()
			}
		}
		val task = viewModelScope.launch {
			taskStartedByIntent()
		}
		longRunningJobs[taskTriggerIntent] = task
	}

	private fun subscribeToIntents() {
		viewModelScope.launch {
			_intent.collect {
				reduceInternal(it, _viewState.value)
				launch {
					performSideEffects(it, _viewState.value)?.let { newIntent -> sendIntent(newIntent) }
				}
			}
		}
	}
}