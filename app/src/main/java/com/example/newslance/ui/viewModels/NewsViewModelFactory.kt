package com.example.newslance.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newslance.NewsApplication
import com.example.newslance.repositories.NewsRepository

class NewsViewModelFactory(val app: Application, private val  newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return NewsViewModel(app, newsRepository) as T
    }
}