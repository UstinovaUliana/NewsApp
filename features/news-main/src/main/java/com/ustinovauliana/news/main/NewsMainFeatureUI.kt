package com.ustinovauliana.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsViewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(newsViewModel: NewsMainViewModel) {
    val state by newsViewModel.state.collectAsState()
    when (val currentState = state) {
        is State.Success -> Articles(currentState.articles)
        is State.Error -> ArticlesWithError(currentState.articles)
        is State.Loading -> ArticlesDuringUpdate(currentState.articles)
        State.None -> NewsEmpty()
    }
}

@Preview
@Composable
private fun Articles(
    @PreviewParameter(ArticlesUIPreviewProvider::class, limit = 1) articles: List<ArticleUI>
) {
    LazyColumn {
        items(articles) { article ->
            key(article.id) {
                Article(article)
            }
        }
    }
}

@Preview
@Composable
internal fun Article(
    @PreviewParameter(
        ArticleUIPreviewProvider::class,
        limit = 1
    ) article: ArticleUI
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = article.title ?: "", style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = article.description ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 3)
    }
}

private class ArticleUIPreviewProvider : PreviewParameterProvider<ArticleUI> {

    override val values = sequenceOf(
        ArticleUI(1, "Title1", "Description1", null, ""),
        ArticleUI(2, "Title2", "Description2", null, ""),
        ArticleUI(3, "Title3", "Description3", null, ""),
        ArticleUI(4, "Title4", "Description4", null, ""),
        ArticleUI(5, "Title5", "Description5", null, ""),
    )
}

private class ArticlesUIPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {

    private val articleProvider = ArticleUIPreviewProvider()
    override val values = sequenceOf(
        articleProvider.values.toList()
    )
}

@Composable
internal fun NewsEmpty() {
    Box(contentAlignment = Alignment.Center) {
        Text(" No news")
    }
}

@Composable
internal fun ArticlesDuringUpdate(
    @PreviewParameter(ArticlesUIPreviewProvider::class, limit = 1) articles: List<ArticleUI>?
) {
    Column {
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        if (articles != null) {
            Articles(articles = articles)
        } else {
            NewsEmpty()
        }
    }
}

@Composable
internal fun ArticlesWithError(articles: List<ArticleUI>?) {
    Column {
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        )
        {
            Text(text = "Error during update", color = MaterialTheme.colorScheme.onError)
        }
        if (articles != null) {
            Articles(articles = articles)
        } else {
            NewsEmpty()
        }
    }
}
