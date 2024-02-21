package com.example.adaapaindonesia.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.adapter.AllNewsAdapter
import com.example.adaapaindonesia.adapter.HeadlineAdapter
import com.example.adaapaindonesia.databinding.ActivityMainBinding
import com.example.adaapaindonesia.factory.NewsViewModelFactory
import com.example.adaapaindonesia.model.ArticlesItem
import com.example.adaapaindonesia.repository.NewsRepository
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import com.example.adaapaindonesia.retrofit.Endpoint.Companion.API_KEY
import com.example.adaapaindonesia.viewmodel.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentDashboard = DashboardFragment()
        val fragmentSearch = SearchFragment()
        val fragmentSavedNews = SavedFragment()
        val fragmentProfile = ProfileFragment()
        setFragment(fragmentDashboard)

        binding.bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.dashboard_icon -> {
                    setFragment(fragmentDashboard)
                    return@setOnItemSelectedListener true
                }
                R.id.search_icon -> {
                    setFragment(fragmentSearch)
                    return@setOnItemSelectedListener true
                }
                R.id.savednews_icon -> {
                    setFragment(fragmentSavedNews)
                    return@setOnItemSelectedListener true
                }
                R.id.profile_icon -> {
                    setFragment(fragmentProfile)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    Toast.makeText(this, "Masih belum diset yaahh", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }


}