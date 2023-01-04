package com.example.newslance.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newslance.R
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)

        val newsRepository = NewsRepository(ArticleDatabase.getDatabase(this))
        val newsViewModelFactory = NewsViewModelFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this,newsViewModelFactory).get(NewsViewModel::class.java)


    }
}