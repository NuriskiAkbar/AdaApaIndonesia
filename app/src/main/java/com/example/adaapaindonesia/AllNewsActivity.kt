package com.example.adaapaindonesia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adaapaindonesia.databinding.ActivityAllNewsBinding
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeoutException

class AllNewsActivity : AppCompatActivity() {

    private val dataAllNewsList : MutableList<ArticlesItem> = mutableListOf()
    private lateinit var binding: ActivityAllNewsBinding
    var page = 1
    var isLoading = false
    var isLastPage = false
    var query = "indonesia"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val title = bundle?.getString("AllNews")


        when(title){
            "AllNews" -> {
                binding.tvDisplayMenu.text = "All News"
               query = "indonesia"
            }
            "business" -> {
                binding.tvDisplayMenu.text = "#bisnis"
                query = "indonesia business"
            }
            "economy" -> {
                binding.tvDisplayMenu.text = "#ekonomi"
                query = "indonesia economy"
            }
            "entertainment" -> {
                binding.tvDisplayMenu.text = "#hiburan"
                query = "indonesia entertainment"
            }
            "technology" -> {
                binding.tvDisplayMenu.text = "#teknologi"
                query = "indonesia technology"
            }
            "health" -> {
                binding.tvDisplayMenu.text = "#kesehatan"
                query = "indonesia health"
            }
            "sport" -> {
                binding.tvDisplayMenu.text = "#olahraga"
                query = "indonesia sport"
            }
            "general" ->{
                binding.tvDisplayMenu.text = "#umum"
                query = "indonesia general"
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callApiGetAllNews = async { ApiService.getInstance().create(Endpoint::class.java).getAllNews(query,10,
                   page, Endpoint.API_KEY
                ) }

                val Response = callApiGetAllNews.await()

                withContext(Dispatchers.Main){
                    if (Response.isSuccessful){
                        val dataNews = Response.body()!!.articles

                        DisplayData(dataNews)
                    } else {
                        Log.d("error luh", Response.message())
                        Toast.makeText(this@AllNewsActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: TimeoutException){

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun DisplayData(dataNews: List<ArticlesItem>?) {
        binding.progressBar3.visibility = View.GONE
        binding.rvAllNewsOnpage.visibility = View.VISIBLE

        binding.rvAllNewsOnpage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAllNewsOnpage.setHasFixedSize(true)
        val adapter = AllNewsAdapter(dataAllNewsList)
        binding.rvAllNewsOnpage.adapter = adapter

        binding.rvAllNewsOnpage.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if(dy>0){
                    val lm = recyclerView.layoutManager as LinearLayoutManager
                    val vItem = lm.childCount
                    val lItem = lm.findFirstVisibleItemPosition()
                    val count = adapter.itemCount

                    if (!isLoading){
                        if (vItem + lItem >= count){
                            addMoreData()
                        }
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        dataAllNewsList.addAll(dataNews.orEmpty())
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMoreData() {
        page++
        isLoading = true
        binding.progressBar4.visibility = View.VISIBLE
        isLastPage = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callApiGetAllNews = async { ApiService.getInstance().create(Endpoint::class.java).getAllNews(query,10,
                    page, Endpoint.API_KEY
                ) }

                val Response = callApiGetAllNews.await()

                withContext(Dispatchers.Main){
                    if (Response.isSuccessful){
                        val dataNews = Response.body()!!.articles
                        if (dataNews != null){
                            dataAllNewsList.addAll(dataNews)
                        } else {
                            isLastPage = true
                            val layoutManager = binding.rvAllNewsOnpage.layoutManager as LinearLayoutManager
                            layoutManager.scrollToPositionWithOffset(0, 0)
                        }
                        Handler().postDelayed({
                            isLoading = false
                            binding.progressBar4.visibility = View.GONE
                            binding.rvAllNewsOnpage.adapter?.notifyDataSetChanged()
                        }, 3000)
                    } else {
                        Log.d("error luh", Response.message())
                        Toast.makeText(this@AllNewsActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: TimeoutException){

            }
        }
    }

}