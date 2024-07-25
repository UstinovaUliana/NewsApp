package com.ustinovauliana.news.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.ustinovauliana.news.NewsTheme

@Composable
internal fun Articles(
    @PreviewParameter(ArticlesUIPreviewProvider::class, limit = 1) articles: List<ArticleUI>,
    navController: NavController
) {
    LazyColumn {
        items(articles) { article ->
            key(article.id) {
                Article(article, navController)
            }
        }
    }
}

@Composable
internal fun Article(
    @PreviewParameter(
        ArticleUIPreviewProvider::class,
        limit = 1
    ) article: ArticleUI,
    navController: NavController
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .padding(bottom = 4.dp)
            .clickable {
                navController.navigate("ArticleDetailsScreen")
            }
    ) {
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
