package com.example.newslance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newslance.newsapi.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}