package com.mvi.example.splitmvi.presentation.list

import com.mvi.example.splitmvi.domain.usecases.GetArticleUseCase
import com.mvi.example.splitmvi.domain.usecases.LoadArticlesUseCase
import com.mvi.mvi.mvi.Mvi

internal class ArticleListMvi(
    private val loadArticlesUseCase: LoadArticlesUseCase = LoadArticlesUseCase(),
    private val getArticleUseCase: GetArticleUseCase = GetArticleUseCase(),
) : Mvi<ArticleListState, ArticleListIntent, ArticleListSingleEvent>() {

    init {
        sendIntent(ArticleListIntent.LoadArticles)
    }

    override fun initialState(): ArticleListState = ArticleListState.LoadingArticles

    override fun reduce(
        intent: ArticleListIntent,
        prevState: ArticleListState
    ): ArticleListState = when (intent) {
        ArticleListIntent.LoadArticles -> prevState
        is ArticleListIntent.OpenArticle -> prevState
        is ArticleListIntent.ShowArticles -> ArticleListState.ArticleList(intent.articleList)
    }

    override suspend fun performSideEffects(
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