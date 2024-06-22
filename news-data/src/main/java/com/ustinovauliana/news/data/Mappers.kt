package com.ustinovauliana.news.data

import com.ustinovauliana.news.data.model.ArticleRepoObj
import com.ustinovauliana.news.data.model.Source
import com.ustinovauliana.newsapi.models.ArticleDTO
import com.ustinovauliana.newsapi.models.SourceDTO
import com.ustinovauliana.newsdatabase.models.ArticleDBO
import com.ustinovauliana.newsdatabase.models.Source as SourceDBO


internal fun ArticleDBO.toArticle() : ArticleRepoObj {
    return ArticleRepoObj(
        id = id,
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun SourceDBO.toSource(): Source {
    return Source(id = id ?: name, name = name)
}

internal fun SourceDTO.toSource(): Source {
    return Source(id = id ?: name, name = name)
}

internal fun SourceDTO.toSourceDbo(): SourceDBO {
    return SourceDBO(id = id ?: name, name = name)
}

internal fun ArticleDTO.toArticle() : ArticleRepoObj {
    return ArticleRepoObj(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}

internal fun ArticleDTO.toArticleDBO(): ArticleDBO {
    return ArticleDBO(
        source = source?.toSourceDbo(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
}
