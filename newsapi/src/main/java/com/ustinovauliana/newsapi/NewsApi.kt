package com.ustinovauliana.newsapi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import com.ustinovauliana.newsapi.models.ArticleDTO
import com.ustinovauliana.newsapi.models.Language
import com.ustinovauliana.newsapi.models.ResponseDTO
import com.ustinovauliana.newsapi.models.SortBy
import com.ustinovauliana.newsapi.utils.NewsApiKeyInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/*
 API details: https://newsapi.org/docs/endpoints/everything
 */
interface NewsApi {
    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @androidx.annotation.IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @androidx.annotation.IntRange(from = 1) page: Int = 1
        ): Result<ResponseDTO<ArticleDTO>>
}

fun newsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json,
): NewsApi {
    return retrofit(baseUrl, apiKey, okHttpClient, json).create(NewsApi::class.java)
}

@OptIn(ExperimentalSerializationApi::class)
private fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json,
): Retrofit {
    val newOkHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(newOkHttpClient)
        .build()
}