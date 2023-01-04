package com.example.newslance.newsapi


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)