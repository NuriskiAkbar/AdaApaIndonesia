package com.example.adaapaindonesia.repository

import com.example.adaapaindonesia.model.ResponseGetAllNews
import com.example.adaapaindonesia.model.ResponseGetTopHeadlines
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import retrofit2.Response

class NewsRepository(private val apiService: Endpoint) {
    suspend fun getHeadlineNews(page: Int) : Response<ResponseGetTopHeadlines>{
        return apiService.getHeadlineNews(page = page)
    }

    suspend fun getAllNews(page: Int) :  Response<ResponseGetAllNews>{
        return apiService.getAllNews(page = page)
    }

    suspend fun getSearchedNews(query:String, page: Int) : Response<ResponseGetAllNews>{
        return apiService.getSearchedNews(query = query, page = page)
    }
}