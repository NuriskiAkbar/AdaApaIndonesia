package com.example.adaapaindonesia.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaapaindonesia.model.ArticlesItem
import com.example.adaapaindonesia.model.ArticlesItemHeadline
import com.example.adaapaindonesia.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository): ViewModel() {
   private val headlineNews = MutableLiveData<List<ArticlesItemHeadline>?>()
   val headlineNewsItem : MutableLiveData<List<ArticlesItemHeadline>?> = headlineNews

    private val allNews = MutableLiveData<List<ArticlesItem>>()
    val allNewsItem : LiveData<List<ArticlesItem>> = allNews

    private val searchedNews = MutableLiveData<List<ArticlesItem>>()
    val searchedNewsItem : LiveData<List<ArticlesItem>> = searchedNews
    private var currentPage = 1

    fun getHeadlineNews(page: Int){
        viewModelScope.launch {
            val response = newsRepository.getHeadlineNews(page)
            if (response.isSuccessful){
                val newData = response.body()?.articles ?: emptyList()
                val totalPage = response.body()?.totalResults
                val currentData = headlineNews.value ?: emptyList()
                val updatedData = currentData.toMutableList()
                    .apply { addAll(newData as Collection<ArticlesItemHeadline>) }

                if (page < totalPage!!){
                    headlineNews.value = updatedData
                    currentPage = page
                    Log.d("page","$page | $updatedData")
                } else {
                    Log.d("ini sudah halaman terakhir","yuhuu")
                }
            } else {
                Log.d("error nih","iya yah kok bisa error yah")
            }
        }
    }

    fun getAllNews(page: Int){
        viewModelScope.launch {
            val response = newsRepository.getAllNews(page)
            if (response.isSuccessful){
                val newData = response.body()?.articles ?: emptyList()
                val currentData = allNews.value ?: emptyList()
                val updatedData = currentData.toMutableList().apply { addAll(newData) }
                allNews.value = updatedData
                currentPage = page
            } else {
                Log.d("error nih","iya yah kok bisa error yah")
            }
        }
    }

    fun getSearchedNews(query: String, page: Int){
        viewModelScope.launch {
            val response = newsRepository.getSearchedNews(query, page)
            if (response.isSuccessful){
                val searchedData = response.body()?.articles ?: emptyList()
                val currentData = searchedNews.value ?: emptyList()
                val updatedData = currentData.toMutableList().apply { addAll(searchedData) }
                searchedNews.value = updatedData
                currentPage = page
            } else {
                Log.d("error nih","iya yah kok error yah")
            }
        }
    }

    fun loadMoreHeadlineNews(){
        val nextPage = currentPage++
        getHeadlineNews(nextPage)
    }

    fun loadMoreAllNews(){
        val nextPage = currentPage++
        getAllNews(nextPage)
    }
}