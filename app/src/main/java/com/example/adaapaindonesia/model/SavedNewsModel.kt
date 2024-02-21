package com.example.adaapaindonesia.model

data class SavedNews(
                     val id: Int,
                     val publishedAt: String,
                     val author: String,
                     val urlToImage: String,
                     val description: String,
                     val source: String,
                     val title: String,
                     val url: String,
                     val content: String)