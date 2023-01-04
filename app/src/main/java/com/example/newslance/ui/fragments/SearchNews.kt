package com.example.newslance.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslance.Constants
import com.example.newslance.R
import com.example.newslance.Resource
import com.example.newslance.adapters.NewsAdapter
import com.example.newslance.ui.activities.MainActivity
import com.example.newslance.ui.viewModels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNews : Fragment(R.layout.fragment_search_news)
{
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).newsViewModel

        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNews_to_articleFragment, bundle
            )
        }



        var job: Job? = null
        etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success->{
                    hideProgressBar()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isAtLastPage = viewModel.searchNewsPageNumber ==totalPages
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    it.message?.let { message->
                        Toast.makeText(activity, "An error Occurred $message", Toast.LENGTH_SHORT).show()
                        Log.e("ERROR", "An Error Occurred: $it")
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
        //return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    /**
     * Setting Up pagination for breaking news
     * Anonymous Class : RecyclerView.onScrollListner
     */

    //List of all booleans
    var isScrolling = false
    var isAtLastPage = false
    var isLoading = false

    var onScrollListner = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndIsNotAtLastPage = !isLoading && !isAtLastPage
            val isAtLastItem = firstVisibleItem + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItem > 0
            val isTotalMoreThanVisible = totalItemCount > Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndIsNotAtLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate==true){
                viewModel.getSearchNews(etSearch.text.toString())
                isScrolling = false
            }
        }
    }


    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNews.onScrollListner)
        }
    }
}
