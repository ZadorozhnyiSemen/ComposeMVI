package com.mvi.example.splitmvi.domain.usecases

import com.mvi.example.splitmvi.data.ArticlesRepository
import com.mvi.example.splitmvi.domain.entity.Article

internal class GetArticleUseCase {

    suspend operator fun invoke(articleId: Int): Article {
        val article = ArticlesRepository.getArticleById(articleId)
        return requireNotNull(article) { "Article not found" }
    }
}