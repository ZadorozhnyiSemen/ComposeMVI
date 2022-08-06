package com.mvi.example.splitmvi.presentation.list

import com.mvi.example.splitmvi.domain.entity.Article
import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State

internal interface ArticleListState : State {
    object LoadingArticles : ArticleListState
    data class ArticleList(val articles: List<Article>) : ArticleListState
}

internal sealed interface ArticleListIntent : Intent {
    object LoadArticles : ArticleListIntent
    data class ShowArticles(val articleList: List<Article>) : ArticleListIntent
    data class OpenArticle(val articleId: Int) : ArticleListIntent
}

internal sealed interface ArticleListSingleEvent : SingleEvent {
    object ArticlesLoaded : ArticleListSingleEvent
}