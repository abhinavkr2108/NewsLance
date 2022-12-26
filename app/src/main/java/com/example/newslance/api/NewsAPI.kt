package com.example.newslance.api

import com.example.newslance.Constants.Companion.API_KEY
import com.example.newslance.newsapi.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")countryCode:String="in",
        @Query("page")pageNumber: Int,
        @Query("apiKey")apiKey: String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")searchQuery:String,
        @Query("page")pageNumber: Int,
        @Query("apiKey")apiKey: String=API_KEY
    ):Response<NewsResponse>


}