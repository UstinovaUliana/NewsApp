package com.ustinovauliana.newsapi.models

import com.ustinovauliana.newsapi.utils.DateTimeUTCSerializer
import kotlinx.serialization.SerialName
import java.util.*

@kotlinx.serialization.Serializable
data class Article(
    @SerialName("source") val source: Source,
    @SerialName("author") val author: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("urlToImage") val urlToImage: String,
    @SerialName("publishedAt") @kotlinx.serialization.Serializable(with = DateTimeUTCSerializer::class) val publishedAt: Date,
    @SerialName("content") val content: String,
)

@kotlinx.serialization.Serializable
data class Source(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)