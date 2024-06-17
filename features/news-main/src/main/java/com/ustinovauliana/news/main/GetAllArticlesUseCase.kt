package com.ustinovauliana.news.main

import com.ustinovauliana.news.data.ArticlesRepository
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<Article> {
       // return repository.getAll()
        TODO("not implemented")
    }
}