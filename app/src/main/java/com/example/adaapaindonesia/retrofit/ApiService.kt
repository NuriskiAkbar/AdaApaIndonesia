package com.example.adaapaindonesia.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"
        private var retrofit: Retrofit? = null

        fun getInstance() : Retrofit{
            if (retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}