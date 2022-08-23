package com.mvi.example.splitmvi.presentation.list

import com.mvi.example.splitmvi.domain.usecases.LoadArticlesUseCase
import com.mvi.mvi.mvi.MviProcessor

internal class ArticleListMviProcessor(
    private val loadArticlesUseCase: LoadArticlesUseCase = LoadArticlesUseCase(),
) : MviProcessor<ArticleListState, ArticleListIntent, ArticleListSingleEvent>() {

    override val reducer: Reducer<ArticleListState, ArticleListIntent>
        get() = ArticleListReducer()

    init {
        sendIntent(ArticleListIntent.LoadArticles)
    }

    override fun initialState(): ArticleListState = ArticleListState.LoadingArticles

    override suspend fun handleIntent(
        intent: ArticleListIntent,
        state: ArticleListState
    ): ArticleListIntent? = when (intent) {
        ArticleListIntent.LoadArticles -> {
            val articles = loadArticlesUseCase()
            ArticleListIntent.ShowArticles(articleList = articles)
        }
        is ArticleListIntent.OpenArticle -> null
        is ArticleListIntent.ShowArticles -> {
            triggerSingleEvent(ArticleListSingleEvent.ArticlesLoaded)
            null
        }
    }
}

internal class ArticleListReducer : MviProcessor.Reducer<ArticleListState, ArticleListIntent> {
    override fun reduce(
        state: ArticleListState,
        intent: ArticleListIntent
    ): ArticleListState = when (intent) {
        ArticleListIntent.LoadArticles -> state
        is ArticleListIntent.OpenArticle -> state
        is ArticleListIntent.ShowArticles -> ArticleListState.ArticleList(intent.articleList)
    }
}
