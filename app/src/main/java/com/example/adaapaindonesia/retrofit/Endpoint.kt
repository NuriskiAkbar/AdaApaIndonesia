package com.example.adaapaindonesia.retrofit

import com.example.adaapaindonesia.ResponseGetAllNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Endpoint {

    companion object{
        const val API_KEY = "97066feb120a4f55bb8ceb9073378f9f"
    }

    @GET("top-headlines")
    suspend fun getHeadlineNews(
        @Query("country") country:String,
        @Query("pageSize") pageSize:Int,
        @Query("page") page:Int,
        @Query("apiKey") apiKey:String
    ) : Response<ResponseGetAllNews>

    @GET("everything")
    suspend fun getAllNews(
        @Query("q") q:String,
        @Query("pageSize") pageSize:Int,
        @Query("page") page:Int,
        @Query("apiKey") apiKey:String
    ) : Response<ResponseGetAllNews>
}