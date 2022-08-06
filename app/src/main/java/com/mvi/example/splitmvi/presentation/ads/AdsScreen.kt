package com.mvi.example.splitmvi.presentation.ads

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvi.example.splitmvi.domain.entity.Advertisement
import com.mvi.example.ui.theme.ComposeMVITheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AdsScreenHolder(
    modifier: Modifier = Modifier,
    mvi: AdsMvi = viewModel()
) {
    val state by mvi.viewState.collectAsStateWithLifecycle()

    AdsScreen(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun AdsScreen(
    state: AdsState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(all = 24.dp)
        ) {
            if (state.currentAd == null) {
                CircularProgressIndicator()
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.currentAd.company, style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = state.currentAd.adText, style = MaterialTheme.typography.h4)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AdsScreenPreview() {
    ComposeMVITheme {
        AdsScreen(
            state = AdsState(
                currentAd = Advertisement(1, "Test", "Test"),
            ),
        )
    }
}