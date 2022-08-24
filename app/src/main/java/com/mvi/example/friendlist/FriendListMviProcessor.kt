package com.mvi.example.friendlist

import com.mvi.mvi.mvi.MviProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal class FriendListMviProcessor constructor() : MviProcessor<
	FriendListState,
	FriendListIntent,
	FriendListSingleEvent
	>() {

	override val reducer: Reducer<FriendListState, FriendListIntent>
		get() = FriendListReducer()

	override fun initialState(): FriendListState = FriendListState()

	override suspend fun handleIntent(
		intent: FriendListIntent,
		state: FriendListState
	): FriendListIntent? = when (intent) {
		FriendListIntent.LoadFriends -> {

			delay(4000)
			val friends = getFriendsUseCase()
			FriendListIntent.ShowFriends(friends)
		}
		is FriendListIntent.ShowFriends -> null
	}

	private suspend fun getFriendsUseCase(): List<Friend> = withContext(Dispatchers.Default) {
		listOf(
			Friend("Don"),
			Friend("Jim"),
			Friend("Tracy"),
		)
	}
}

internal class FriendListReducer : MviProcessor.Reducer<FriendListState, FriendListIntent> {
	override fun reduce(
		state: FriendListState,
		intent: FriendListIntent
	): FriendListState = when (intent) {
		FriendListIntent.LoadFriends -> {
			state.copy(
				canLoad = false,
				isLoading = true,
				friendList = listOf(),
			)
		}
		is FriendListIntent.ShowFriends -> {
			state.copy(
				canLoad = intent.friendList.isEmpty(),
				isLoading = false,
				friendList = intent.friendList,
			)
		}
	}
}