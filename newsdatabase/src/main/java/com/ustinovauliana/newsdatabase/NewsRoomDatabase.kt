package com.ustinovauliana.newsdatabase

import android.content.Context
import androidx.room.*
import com.ustinovauliana.newsdatabase.dao.ArticleDao
import com.ustinovauliana.newsdatabase.models.ArticleDBO
import com.ustinovauliana.newsdatabase.utils.Converters

class NewsDatabase internal constructor (private val database: NewsRoomDatabase) {
    val articlesDao: ArticleDao
        get() = database.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase: RoomDatabase() {

    abstract fun articlesDao(): ArticleDao
}

fun NewsDatabase(applicationContext: Context) : NewsDatabase {
    val db = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDatabase::class.java,
        "news"
    ).build()
    return NewsDatabase(db)
}
