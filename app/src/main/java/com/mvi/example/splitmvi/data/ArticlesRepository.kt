package com.mvi.example.splitmvi.data

import com.mvi.example.splitmvi.domain.entity.Article
import kotlinx.coroutines.delay

object ArticlesRepository {

    private val articles = listOf(
        Article(1, "ArticleTitle1", "ArticleText1"),
        Article(2, "ArticleTitle2", "ArticleText2"),
        Article(3, "ArticleTitle3", "ArticleText3"),
        Article(4, "ArticleTitle4", "ArticleText4"),
        Article(5, "ArticleTitle5", "ArticleText5"),
        Article(6, "ArticleTitle6", "ArticleText6"),
        Article(7, "ArticleTitle7", "ArticleText7"),
    )

    suspend fun getAllArticles(): List<Article> {
        delay(2000)
        return articles
    }

    suspend fun getArticleById(id: Int): Article? {
        delay(1000)
        return articles.find { it.id == id }
    }
}