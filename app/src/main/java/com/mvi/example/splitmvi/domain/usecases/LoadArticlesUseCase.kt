package com.mvi.example.splitmvi.domain.usecases

import com.mvi.example.splitmvi.data.ArticlesRepository
import com.mvi.example.splitmvi.domain.entity.Article

internal class LoadArticlesUseCase {

    suspend operator fun invoke(): List<Article> {
        return ArticlesRepository.getAllArticles()
    }
}