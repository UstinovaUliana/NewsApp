package com.ustinovauliana.news.main

import com.ustinovauliana.news.data.ArticlesRepository
import com.ustinovauliana.news.data.RequestResult
import com.ustinovauliana.news.data.map
import com.ustinovauliana.news.data.model.ArticleRepoObj
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(private val repository: ArticlesRepository) {

    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query)
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticles() }
                }
            }
    }
}

private fun ArticleRepoObj.toUiArticles(): ArticleUI {
    return ArticleUI(
        id = id,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url
    )
}
