package com.ustinovauliana.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ustinovauliana.news.data.ArticlesRepository
import com.ustinovauliana.news.data.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    private val getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
    private val repository: ArticlesRepository,
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun forceUpdate() {
        val requestResultFlow = repository.fetchLatest()
    }
}

private fun RequestResult<List<Article>>.toState(): State {
    return when (this) {
        is RequestResult.Success -> State.Success(data)
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
    }
}

sealed class State {

    object None : State()
    class Loading(val articles: List<Article>? = null) : State()
    class Error(val articles: List<Article>? = null) : State()
    class Success(val articles: List<Article>) : State()
}