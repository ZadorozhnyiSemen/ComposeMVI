package com.mvi.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mvi.example.timer.TimerScreenHolder
import com.mvi.example.ui.theme.ComposeMVITheme

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ComposeMVITheme {
//				FriendListScreenHolder()
				TimerScreenHolder()
			}
		}
	}
}
