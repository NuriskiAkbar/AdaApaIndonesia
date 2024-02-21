package com.example.adaapaindonesia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.adaapaindonesia.model.SavedNews
import com.example.adaapaindonesia.sqlite.SavedNewsDatabase

class SavedNewsViewModel(private val savedNewsDao : SavedNewsDatabase): ViewModel() {

    val savedNews: List<SavedNews> = savedNewsDao.getAllSavedNews()
    fun insertNews(savedNews: SavedNews){
        savedNewsDao.Insert(savedNews)
    }

    fun deleteNews(id: Int){
        savedNewsDao.delete(id)
    }

    fun getAllSavedNews(){
        savedNewsDao.getAllSavedNews()
    }
}