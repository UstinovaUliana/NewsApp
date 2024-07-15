package com.ustinovauliana.newsapi.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class SortBy {
    @SerialName("relevancy")
    RELEVANCY,

    @SerialName("popularity")
    POPULARITY,

    @SerialName("publishedAt")
    PUBLISHED_AT
}
