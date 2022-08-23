@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.mvi.example.friendlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun FriendListScreenHolder(
	mvi: FriendListMviProcessor = viewModel()
) {

	val state by mvi.viewState.collectAsStateWithLifecycle()

	FriendListScreen(
		state = state,
		onStarted = { mvi.sendIntent(FriendListIntent.LoadFriends) },
	)
}

@Composable
private fun FriendListScreen(
	state: FriendListState,
	onStarted: () -> Unit,
) {
	Box(modifier = Modifier.fillMaxSize()) {
		if (state.friendList.isNotEmpty()) {
			Column {
				state.friendList.forEach {
					Text(it.name)
				}
			}
		}

		if (state.isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.align(Alignment.Center),
			)
		}

		if (state.isLoadingButtonVisible) {
			Button(
				modifier = Modifier.align(Alignment.Center),
				onClick = onStarted,
			) {
				Text("Load friends")
			}
		}
	}
}
