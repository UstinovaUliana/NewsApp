package com.ustinovauliana.news.main

import com.ustinovauliana.news.data.ArticlesRepository
import com.ustinovauliana.news.data.RequestResult
import com.ustinovauliana.news.data.map
import com.ustinovauliana.news.data.model.ArticleRepoObj
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<Article>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticles() }
                }
            }
    }
}

private fun ArticleRepoObj.toUiArticles(): Article {
    TODO("Not yet implemented")
}
