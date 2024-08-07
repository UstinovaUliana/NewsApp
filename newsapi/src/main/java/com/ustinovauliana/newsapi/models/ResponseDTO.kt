package com.ustinovauliana.newsapi.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ResponseDTO<E>(
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int,
    @SerialName("articles")
    val articles: List<E>
)
