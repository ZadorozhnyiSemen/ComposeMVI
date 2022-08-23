package com.mvi.example.splitmvi.presentation.list

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
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
import com.mvi.example.splitmvi.domain.entity.Article
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun ArticleListScreenHolder(
    modifier: Modifier = Modifier,
    mvi: ArticleListMviProcessor = viewModel()
) {
    val context = LocalContext.current

    val state by mvi.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mvi.singleEvent
            .onEach {
                when (it) {
                    ArticleListSingleEvent.ArticlesLoaded -> {
                        Toast.makeText(context, "Articles loaded", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .collect()
    }

    ArticleListScreen(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun ArticleListScreen(
    state: ArticleListState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is ArticleListState.LoadingArticles -> LoadingComponent()
            is ArticleListState.ArticleList -> ArticleListComponent(state.articles)
        }
    }
}

@Composable
private fun LoadingComponent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ArticleListComponent(articles: List<Article>) {
    LazyColumn {
        items(articles) {
            ArticleItem(
                it.title,
                it.text,
            )
        }
    }
}

@Composable
private fun ArticleItem(
    title: String,
    text: String,
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text, style = MaterialTheme.typography.caption)
    }
}