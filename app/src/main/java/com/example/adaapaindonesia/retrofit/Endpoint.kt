package com.example.adaapaindonesia.retrofit

import com.example.adaapaindonesia.model.ResponseGetAllNews
import com.example.adaapaindonesia.model.ResponseGetTopHeadlines
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Endpoint {

    companion object{
        const val API_KEY = "befb07d9c50448abb30bcd4a9e8afd3e"
    }

    @GET("top-headlines")
    suspend fun getHeadlineNews(
        @Query("country") q:String = "id",
        @Query("pageSize") pageSize:Int = 10,
        @Query("page") page:Int = 1,
        @Query("apiKey") apiKey:String = API_KEY
    ) : Response<ResponseGetTopHeadlines>

    @GET("everything")
    suspend fun getAllNews(
        @Query("domains") domains:String = "liputan6.com,detik.com,kompas.com,Tribunnews.com",
        @Query("pageSize") pageSize:Int = 10,
        @Query("page") page:Int = 1,
        @Query("apiKey") apiKey:String = API_KEY
    ) : Response<ResponseGetAllNews>

    @GET("everything")
    suspend fun getSearchedNews(
        @Query("domains") domains:String = "liputan6.com,detik.com,kompas.com,Tribunnews.com",
        @Query("q") query:String = "",
        @Query("pageSize") pageSize:Int = 10,
        @Query("page") page:Int = 1,
        @Query("apiKey") apiKey:String = API_KEY
    ) : Response<ResponseGetAllNews>
}