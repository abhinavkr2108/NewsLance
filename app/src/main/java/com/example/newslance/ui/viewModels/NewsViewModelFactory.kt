package com.example.newslance.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newslance.repositories.NewsRepository

class NewsViewModelFactory(private val  newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return NewsViewModel(newsRepository) as T
    }
}