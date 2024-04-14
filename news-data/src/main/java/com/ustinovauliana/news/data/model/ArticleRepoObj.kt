package com.ustinovauliana.news.data.model

import java.util.Date

data class ArticleRepoObj(
    val id: Long,
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Date,
    val content: String,
)

data class Source(
    val id: String,
    val name: String
)