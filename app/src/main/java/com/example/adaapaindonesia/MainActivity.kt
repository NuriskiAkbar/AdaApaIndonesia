package com.example.adaapaindonesia

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.adaapaindonesia.databinding.ActivityMainBinding
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import com.example.adaapaindonesia.retrofit.Endpoint.Companion.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    private val dataHeadlineList : MutableList<ArticlesItem> = mutableListOf()
    private lateinit var binding : ActivityMainBinding
    var isLoading = false
    var isLastPage = false
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar2.visibility = View.VISIBLE
        binding.rvHeadline.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBarPagination.visibility = View.GONE
        binding.rvSemuaBerita.visibility = View.GONE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callApiGetAllNews = async { ApiService.getInstance().create(Endpoint::class.java).getAllNews("indonesia",10, page, API_KEY) }
                val callApiGetAllHeadlineNews = async { ApiService.getInstance().create(Endpoint::class.java).getHeadlineNews("id", 5, page, API_KEY) }

                val Response = callApiGetAllNews.await()
                val ResponseHeadlineNews = callApiGetAllHeadlineNews.await()

                withContext(Dispatchers.Main){
                    if (Response.isSuccessful && ResponseHeadlineNews.isSuccessful){
                        val dataNews = Response.body()!!.articles
                        val dataHeadlineNews = ResponseHeadlineNews.body()!!.articles

                        DisplayData(dataNews)
                        DisplayData2(dataHeadlineNews)
                    } else {
                        Log.d("error luh", "${Response.message()}")
                        Toast.makeText(this@MainActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: TimeoutException){
                Log.d("error luh", e.toString())
                Toast.makeText(this@MainActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvAllNewsLihatSemua.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("AllNews", "AllNews")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnBusiness.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("AllNews", "business")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnEconomy.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "economy")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnEntertainment.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "entertainment")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnTechnology.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "technology")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnHealth.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "health")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnSport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "sport")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnGeneral.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "general")
            startActivity(Intent(this, AllNewsActivity::class.java).putExtras(bundle))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun DisplayData2(dataHeadlineNews: List<ArticlesItem>?) {
        binding.progressBar2.visibility = View.GONE
        binding.rvHeadline.visibility = View.VISIBLE

        binding.rvHeadline.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHeadline.setHasFixedSize(true)
        val adapter = HeadlineAdapter(dataHeadlineList)
        binding.rvHeadline.adapter = adapter

        binding.rvHeadline.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d("scrolled","$dx")
                if(dx > 0){
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

        dataHeadlineList.addAll(dataHeadlineNews.orEmpty())
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMoreData() {
        page++
        isLoading = true
        binding.progressBarPagination.visibility = View.VISIBLE
        isLastPage = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callApiGetAllHeadlineNews = async { ApiService.getInstance().create(Endpoint::class.java).getHeadlineNews("id", 5, page, API_KEY) }
                val ResponseHeadlineNews = callApiGetAllHeadlineNews.await()

                withContext(Dispatchers.Main){
                    if (ResponseHeadlineNews.isSuccessful){
                        val dataHeadlineNews = ResponseHeadlineNews.body()!!.articles
                        if (dataHeadlineNews != null){
                            dataHeadlineList.addAll(dataHeadlineNews)
                        } else {
                            isLastPage = true
                        }
                        Handler().postDelayed({
                            isLoading = false
                            binding.progressBarPagination.visibility = View.GONE
                            //binding.rvHeadline.adapter = HeadlineAdapter(dataHeadlineList)
                            binding.rvHeadline.adapter?.notifyDataSetChanged()
                        }, 3000)
                    } else {
                        Log.d("error luh", ResponseHeadlineNews.message())
                        Toast.makeText(this@MainActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: TimeoutException){
                Log.d("error luh", e.toString())
                Toast.makeText(this@MainActivity, " error yah, silahkan keluar dan masuk lagi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun DisplayData(dataNews: List<ArticlesItem>?) {
        binding.progressBar.visibility = View.GONE
        binding.rvSemuaBerita.visibility = View.VISIBLE

        binding.rvSemuaBerita.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSemuaBerita.setHasFixedSize(true)
        val adapter = AllNewsAdapter(dataNews!!)
        binding.rvSemuaBerita.adapter = adapter

        adapter.setOnItemClickListener { position ->
            Toast.makeText(this, "Masih dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}