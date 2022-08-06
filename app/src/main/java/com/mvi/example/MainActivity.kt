package com.mvi.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mvi.example.friendlist.FriendListScreenHolder
import com.mvi.example.gamelobby.presentation.GameLobbyScreenHolder
import com.mvi.example.splitmvi.presentation.SplitScreenHolder
import com.mvi.example.timer.TimerScreenHolder
import com.mvi.example.ui.theme.ComposeMVITheme


class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ComposeMVITheme {
				val navController = rememberNavController()

				NavHost(navController = navController, startDestination = "main") {
					composable("main") { MainScreen(navController) }
					composable("friendslist") { FriendListScreenHolder() }
					composable("timer") { TimerScreenHolder() }
					composable("split") { SplitScreenHolder() }
					composable("game") { GameLobbyScreenHolder() }
				}
			}
		}
	}
}

@Composable
fun MainScreen(navController: NavHostController) {
	Column {
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = { navController.navigate("friendslist") }
		) {
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Friend List Example",
			)
		}
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = { navController.navigate("timer") }
		) {
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Timer example",
			)
		}
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = { navController.navigate("split") }
		) {
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Split Mvi example"
			)
		}
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = { navController.navigate("game") }
		) {
			Text(
				modifier = Modifier.padding(16.dp),
				text = "Game Lobby example",
			)
		}
	}
}