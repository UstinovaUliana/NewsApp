package com.ustinovauliana.newsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ustinovauliana.newsdatabase.dao.ArticleDao
import com.ustinovauliana.newsdatabase.models.ArticleDBO
import com.ustinovauliana.newsdatabase.utils.Converters

class NewsDatabase internal constructor(private val database: NewsRoomDatabase) {
    val articlesDao: ArticleDao
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticleDao
}

fun NewsDatabase(applicationContext: Context): NewsDatabase {
    return NewsDatabase(
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            NewsRoomDatabase::class.java,
            "news"
        ).build()
    )
}
