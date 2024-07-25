package com.ustinovauliana.newsapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ustinovauliana.news.NewsTheme
import com.ustinovauliana.news.main.ArticleDetailsScreen
import com.ustinovauliana.news.main.NewsMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentScreen by navController.currentBackStackEntryAsState()
            NewsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {
                        TopAppBar(
                            title = { Text(text = currentScreen?.destination?.route ?: "") },
                            navigationIcon = {
                                if (currentScreen?.destination?.route != "NewsMainScreen") {
                                    IconButton(onClick = {
                                        navController.navigateUp()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        )
                    },
                ) { values ->
                    Surface(
                        modifier = Modifier.padding(values)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "NewsMainScreen"
                        ) {
                            composable("NewsMainScreen") {
                                NewsMainScreen(navController = navController)
                            }

                            composable("ArticleDetailsScreen") {
                                ArticleDetailsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
