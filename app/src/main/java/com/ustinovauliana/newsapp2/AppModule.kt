package com.ustinovauliana.newsapp2

import android.content.Context
import com.ustinovauliana.newsapi.NewsApi
import com.ustinovauliana.newsdatabase.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase(context)
    }

}