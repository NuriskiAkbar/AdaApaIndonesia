package com.example.adaapaindonesia.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adaapaindonesia.sqlite.SavedNewsDatabase
import com.example.adaapaindonesia.viewmodel.SavedNewsViewModel

class SavedNewsModelFactory(private val repository: SavedNewsDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedNewsViewModel::class.java)){
            return SavedNewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknows ViewModel class")
    }
}