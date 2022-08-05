package com.mvi.example.friendlist

import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State

internal data class FriendListState(
	val isLoadingButtonVisible: Boolean = true,
	val isLoading: Boolean = false,
	val friendList: List<Friend> = listOf(),
): State

internal sealed interface SealedFriendListState : State {
	object Idle : SealedFriendListState
	object Loading : SealedFriendListState
	data class FriendList(val friends: List<Friend>) : SealedFriendListState
}

internal sealed interface FriendListIntent : Intent {
	object LoadFriends : FriendListIntent
	data class ShowFriends(val friendList: List<Friend>) : FriendListIntent
}

internal sealed interface FriendListSingleEvent : SingleEvent


data class Friend(
	val name: String
)