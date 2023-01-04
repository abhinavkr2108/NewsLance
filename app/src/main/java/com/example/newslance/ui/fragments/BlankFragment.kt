package com.example.newslance.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newslance.R
import com.example.newslance.adapters.NewsAdapter
import com.example.newslance.ui.viewModels.NewsViewModel


class BlankFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        return view
    }
}