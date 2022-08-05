package com.mvi.example.friendlist

import com.mvi.mvi.mvi.Mvi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal class FriendListMvi constructor() : Mvi<
	FriendListState,
	FriendListIntent,
	FriendListSingleEvent
	>() {

	override fun initialState(): FriendListState = FriendListState()

	override fun reduce(
		intent: FriendListIntent,
		prevState: FriendListState
	): FriendListState = when (intent) {
		FriendListIntent.LoadFriends -> {
			prevState.copy(
				isLoadingButtonVisible = false,
				isLoading = true,
				friendList = listOf(),
			)
		}
		is FriendListIntent.ShowFriends -> {
			prevState.copy(
				isLoadingButtonVisible = intent.friendList.isEmpty(),
				isLoading = false,
				friendList = intent.friendList,
			)
		}
	}

	override suspend fun performSideEffects(
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