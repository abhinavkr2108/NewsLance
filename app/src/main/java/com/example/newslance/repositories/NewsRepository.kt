package com.example.newslance.repositories

import com.example.newslance.api.RetrofitInstance
import com.example.newslance.database.ArticleDatabase
import com.example.newslance.newsapi.Article
import com.example.newslance.newsapi.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>{
        return RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>{
        return RetrofitInstance.api.searchNews(searchQuery,pageNumber)
    }

    suspend fun insertArticle(article: Article) = db.getArticleDao().insert(article)
    fun getArticle() = db.getArticleDao().getArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}