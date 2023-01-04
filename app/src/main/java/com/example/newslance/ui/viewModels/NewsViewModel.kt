package com.example.newslance.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newslance.NewsApplication
import com.example.newslance.Resource
import com.example.newslance.newsapi.Article
import com.example.newslance.newsapi.NewsResponse
import com.example.newslance.repositories.NewsRepository
import com.example.newslance.ui.fragments.BreakingNews
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val repository: NewsRepository): AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPageNumber = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                breakingNewsPageNumber++
                if (breakingNewsResponse==null){
                    breakingNewsResponse = it
                }
                else{
                    val oldArticles = breakingNewsResponse!!.articles
                    val newArticles = it.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                searchNewsPageNumber++
                if (searchNewsResponse==null){
                    searchNewsResponse = it
                }
                else{
                    val oldArticles = searchNewsResponse!!.articles
                    val newArticles = it.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        try {
            if (hasInternetConnection()==true){
                val response = repository.getBreakingNews(countryCode, breakingNewsPageNumber)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }

        }
        catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure Occurred"))
                else -> breakingNews.postValue(Resource.Error("JSON Conversion Error Occurred"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        try {
            if (hasInternetConnection()==true){
                val response = repository.searchNews(searchQuery, searchNewsPageNumber)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }

        }
        catch (t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure Occurred"))
                else -> searchNews.postValue(Resource.Error("JSON Conversion Error Occurred"))
            }
        }
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun getArticle() = repository.getArticle()


    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun hasInternetConnection(): Boolean{
        val  connectivityManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return  when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}