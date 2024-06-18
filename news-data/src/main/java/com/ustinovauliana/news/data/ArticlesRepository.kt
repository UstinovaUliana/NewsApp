package com.ustinovauliana.news.data

import com.ustinovauliana.news.data.model.ArticleRepoObj
import com.ustinovauliana.newsapi.NewsApi
import com.ustinovauliana.newsapi.models.ArticleDTO
import com.ustinovauliana.newsapi.models.ResponseDTO
import com.ustinovauliana.newsdatabase.NewsDatabase
import com.ustinovauliana.newsdatabase.models.ArticleDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val requestResponseMergeStrategy: MergeStrategy<RequestResult<List<ArticleRepoObj>>>,
) {

    fun getAll(): Flow<RequestResult<List<ArticleRepoObj>>> {
        val localArticles = getAllFromDatabase()
            .map { result ->
                result.map { articleDBOs ->
                    articleDBOs.map { it.toArticle() }
                }
            }
        val remoteArticles = getAllFromServer()
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }

        return localArticles.combine(remoteArticles, requestResponseMergeStrategy::merge)
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val articleDBOs = data.map { articleDTO -> articleDTO.toArticleDBO() }
        database.articlesDao.insert(articleDBOs)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao
            .getAll()
            .map {
                RequestResult.Success(it)
            }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
    }

    suspend fun search(query: String): Flow<ArticleRepoObj> {
        TODO("Not implemented")
    }
}
