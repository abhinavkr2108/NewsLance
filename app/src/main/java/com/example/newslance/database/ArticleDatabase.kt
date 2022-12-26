package com.example.newslance.database

import android.content.Context
import androidx.room.*
import com.example.newslance.newsapi.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converter::class)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var INSTANCE:ArticleDatabase? =null

        fun getDatabase(context: Context):ArticleDatabase{
            if (INSTANCE==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java, "articleDB").build()
                }
            }
            return INSTANCE!!
        }

    }

}