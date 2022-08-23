package com.mvi.example.timer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun TimerScreenHolder(
	mvi: TimerMviProcessor = viewModel()
) {
	val context = LocalContext.current

	val state by mvi.viewState.collectAsStateWithLifecycle()

	LaunchedEffect(Unit) {
		mvi.singleEvent.onEach {
			when (it) {
				TimerSingleEvent.ShowNotification -> {
					Toast.makeText(context, "Count down completed", Toast.LENGTH_SHORT).show()
				}
			}
		}.collect()
	}

	TimerScreen(
		state = state,
	)
}

@Composable
private fun TimerScreen(
	state: TimerState
) {
	Box(modifier = Modifier.fillMaxSize()) {
		Text(
			modifier = Modifier.align(Alignment.Center),
			text = state.timeLeft.toString(),
			style = MaterialTheme.typography.h2,
		)
	}
}