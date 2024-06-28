package com.ustinovauliana.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.ustinovauliana.news.NewsTheme

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsViewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(newsViewModel: NewsMainViewModel) {
    val state by newsViewModel.state.collectAsState()
    val currentState = state
    if (state != State.None) {
        NewsMainContent(currentState)
    }
}

@Composable
private fun NewsMainContent(currentState: State) {
    Column {
        if (currentState is State.Error) {
            ErrorMessage(currentState)
        }
        if (currentState is State.Loading) {
            ProgressIndicator(currentState)
        }
        if (currentState.articles != null) {
            Articles(articles = currentState.articles)
        }
    }
}

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

@Composable
internal fun Article(
    @PreviewParameter(
        ArticleUIPreviewProvider::class,
        limit = 1
    ) article: ArticleUI
) {
    Row(Modifier.padding(bottom=4.dp)) {
        article.imageUrl?.let { imageUrl ->
            var isImageVisible by remember {mutableStateOf(true)}
            if (isImageVisible) {
                AsyncImage(
                    model = imageUrl,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) {
                            isImageVisible = false
                        }
                    },
                    contentDescription = stringResource (R.string.content_dedsc_item_article_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = article.title ?: "",
                style = NewsTheme.typography.headlineMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = article.description ?: "",
                style = NewsTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
    }
}

@Suppress("MagicNumber")
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

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(NewsTheme.colorScheme.error)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    )
    {
        Text(text = "Error during update", color = NewsTheme.colorScheme.onError)
    }
}


