package com.ustinovauliana.news.data

import com.ustinovauliana.news.data.model.ArticleRepoObj
import com.ustinovauliana.newsapi.NewsApi
import com.ustinovauliana.newsapi.models.ArticleDTO
import com.ustinovauliana.newsapi.models.ResponseDTO
import com.ustinovauliana.newsdatabase.NewsDatabase
import com.ustinovauliana.newsdatabase.models.ArticleDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(
        mergeStrategy: MergeStrategy<RequestResult<List<ArticleRepoObj>>> = RequestResponseMergeStrategy(),
    ): Flow<RequestResult<List<ArticleRepoObj>>> {
        val localArticles = getAllFromDatabase()

        val remoteArticles = getAllFromServer()

        return localArticles.combine(remoteArticles, mergeStrategy::merge)
           .flatMapLatest { result ->
                  if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
               }
            }
    }

    fun searchArticles(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<ArticleRepoObj>>> = RequestResponseMergeStrategy(),
    ): Flow<RequestResult<List<ArticleRepoObj>>> {
        val localArticles = searchFromDatabase(query)

        val remoteArticles = searchFromServer(query)

        return localArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                    flowOf(result)
            }
    }

    private fun getAllFromServer(): Flow<RequestResult<List<ArticleRepoObj>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(result.getOrThrow().articles)
                }
            }
            .map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private fun searchFromServer(query: String): Flow<RequestResult<List<ArticleRepoObj>>> {
        val apiRequest = flow { emit(api.everything(query = query)) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(result.getOrThrow().articles)
                }
            }
            .map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start)
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val articleDBOs = data.map { articleDTO -> articleDTO.toArticleDBO() }
        database.articlesDao.insert(articleDBOs)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleRepoObj>>> {
        val dbRequest = database.articlesDao::getAll.asFlow()
            .map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> {
                RequestResult.Success(it)
            }
            .catch {
                emit(RequestResult.Error(error = it))
            }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
            .map { result ->
                result.map { articleDBOs ->
                    articleDBOs.map { it.toArticle() }
                }
            }
    }

    private fun searchFromDatabase(query: String): Flow<RequestResult<List<ArticleRepoObj>>> {
        val dbRequest = database.articlesDao.searchArticles(query)
            .map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> {
                RequestResult.Success(it)
            }
            .catch {
                emit(RequestResult.Error(error = it))
            }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
            .map { result ->
                result.map { articleDBOs ->
                    articleDBOs.map { it.toArticle() }
                }
            }
    }
}
