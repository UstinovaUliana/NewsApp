package com.ustinovauliana.news.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ustinovauliana.news.NewsTheme
import com.ustinovauliana.news.main.ui.Articles

@Composable
fun NewsMainScreen() {
    NewsMainScreen(newsViewModel = viewModel())
}

@Composable
@Suppress("LongMethod")
internal fun NewsMainScreen(newsViewModel: NewsMainViewModel) {

    var query: String by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.getTop(density) / density.density

    showClearIcon = query.isNotEmpty()

    val state by newsViewModel.state.collectAsState()
    // val currentState = state
    // newsViewModel.getAll()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = statusBarHeight.dp),
        topBar = {
            SearchField(query, newsViewModel)
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
private fun SearchField(
    query: String,
    newsViewModel: NewsMainViewModel
) {
    var query1 = query
    TextField(
        value = query1,
        onValueChange = { onQueryChanged: String ->
            query1 = onQueryChanged
            if (onQueryChanged.isNotEmpty()) {
                newsViewModel.search(query1)
            } else {
                newsViewModel.getAll()
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
            IconButton(onClick = {
                query1 = ""
                newsViewModel.getAll()
            }) {
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
