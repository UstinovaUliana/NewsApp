package com.ustinovauliana.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
    var query: String by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    if (query.isEmpty()) {
        showClearIcon = false
    } else if (query.isNotEmpty()) {
        showClearIcon = true
    }

    val state by newsViewModel.state.collectAsState()
 //   val currentState = state
    Scaffold(
        topBar = {
            TextField(
                value = query,
                onValueChange = { onQueryChanged: String ->
                    query = onQueryChanged
                    if (onQueryChanged.isNotEmpty()) {
                        newsViewModel.search(query)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = NewsTheme.colorScheme.onBackground,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { query = ""}) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "Clear Icon"
                        )
                    }
                },
                maxLines = 1,
                placeholder = {
                    stringResource(R.string.hint_search_query)
                },
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = NewsTheme.colorScheme.background, shape = RectangleShape),
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (state != State.None) {
                    NewsMainContent(state)
                }
            }
        }
    )
}

@Composable
private fun NewsMainContent(currentState: State) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
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
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Row(Modifier.padding(bottom = 4.dp)) {
        article.imageUrl?.let { imageUrl ->
            var isImageVisible by remember { mutableStateOf(true) }
            if (isImageVisible) {
                AsyncImage(
                    model = imageUrl,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) {
                            isImageVisible = false
                        }
                    },
                    contentDescription = stringResource(R.string.content_dedsc_item_article_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
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
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            )
            Text(
                modifier = Modifier.clickable {
                    isExpanded = !isExpanded
                },
                text = if (!isExpanded) "..." else "hide"
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
    ) {
        Text(text = "Error during update", color = NewsTheme.colorScheme.onError)
    }
}



