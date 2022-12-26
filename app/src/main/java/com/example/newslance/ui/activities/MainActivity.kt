package com.example.newslance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newslance.database.ArticleDatabase
import com.example.newslance.repositories.NewsRepository
import com.example.newslance.ui.viewModels.NewsViewModel
import com.example.newslance.ui.viewModels.NewsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView.setupWithNavController(newsNavHostFragment.findNavController())
        val newsRepository = NewsRepository(ArticleDatabase.getDatabase(this))
        val newsViewModelFactory = NewsViewModelFactory(newsRepository)
        newsViewModel = ViewModelProvider(this,newsViewModelFactory).get(NewsViewModel::class.java)


    }
}