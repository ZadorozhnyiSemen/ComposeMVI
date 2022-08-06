package com.mvi.example.splitmvi.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvi.example.splitmvi.presentation.ads.AdsScreenHolder
import com.mvi.example.splitmvi.presentation.list.ArticleListScreenHolder

@Composable
internal fun SplitScreenHolder() {
    SplitScreen()
}

@Composable
private fun SplitScreen() {
    Column {
        AdsScreenHolder(modifier = Modifier.weight(0.5f))
        ArticleListScreenHolder(modifier = Modifier.weight(0.5f))
    }
}