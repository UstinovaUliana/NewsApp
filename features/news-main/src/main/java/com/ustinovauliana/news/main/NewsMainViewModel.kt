package com.ustinovauliana.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val searchArticlesUseCase: Provider<SearchArticlesUseCase>,
) : ViewModel() {

    var state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun getAll() {
        state = getAllArticlesUseCase.get().invoke()
            .map { it.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
    }

    fun search(query: String) {
        state = searchArticlesUseCase.get().invoke(query = query)
            .map { it.toState() }
            .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
    }
}

private fun RequestResult<List<ArticleUI>>.toState(): State {
    return when (this) {
        is RequestResult.Success -> State.Success(data)
        is RequestResult.Error -> State.Error(data)
        is RequestResult.InProgress -> State.Loading(data)
    }
}

internal sealed class State(val articles: List<ArticleUI>?) {

    data object None : State(articles = null)
    class Loading(articles: List<ArticleUI>? = null) : State(articles)
    class Error(articles: List<ArticleUI>? = null) : State(articles)
    class Success(articles: List<ArticleUI>) : State(articles)
}
