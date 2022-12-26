package com.example.newslance.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.newslance.MainActivity
import com.example.newslance.R
import com.example.newslance.ui.viewModels.NewsViewModel


class BreakingNews : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).newsViewModel
    }



}