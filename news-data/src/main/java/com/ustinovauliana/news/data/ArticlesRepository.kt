package com.ustinovauliana.news.data

import com.ustinovauliana.news.data.model.ArticleRepoObj
import com.ustinovauliana.newsapi.NewsApi
import com.ustinovauliana.newsdatabase.NewsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(): RequestResult<Flow<List<ArticleRepoObj>>> {
        return RequestResult.InProgress(
            database.articlesDao
                .getAll()
                .map { articles ->
                    articles.map {
                        it.toArticle()
                    }
                }
        )
    }

    suspend fun search(query: String): Flow<ArticleRepoObj> {
        TODO("Not implemented")
    }
}

sealed class RequestResult<E>(protected val data: E?) {

    class InProgress<E>(data: E?) : RequestResult<E>(data)
    class Success<E>(data: E?) : RequestResult<E>(data)
    class Error<E>(data: E?) : RequestResult<E>(data)
}