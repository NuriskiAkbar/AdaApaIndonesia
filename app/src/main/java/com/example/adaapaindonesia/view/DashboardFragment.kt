package com.example.adaapaindonesia.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.adapter.AllNewsAdapter
import com.example.adaapaindonesia.adapter.HeadlineAdapter
import com.example.adaapaindonesia.databinding.FragmentDashboardBinding
import com.example.adaapaindonesia.factory.NewsViewModelFactory
import com.example.adaapaindonesia.repository.NewsRepository
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import com.example.adaapaindonesia.viewmodel.NewsViewModel


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapterHeadline: HeadlineAdapter
    private lateinit var adapterAllNews: AllNewsAdapter
    var isLoading = false
    var isLastPage = false
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar2.visibility = View.VISIBLE
        binding.rvHeadline.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBarPagination.visibility = View.GONE
        binding.rvSemuaBerita.visibility = View.GONE

        val apiService = ApiService.getInstance().create(Endpoint::class.java)
        val repository = NewsRepository(apiService)
        viewModel = ViewModelProvider(this, NewsViewModelFactory(repository))[NewsViewModel::class.java]

        binding.rvSemuaBerita.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvHeadline.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.headlineNewsItem.observe(requireActivity()) { response ->
            if (response != null){
                if (!::adapterHeadline.isInitialized){
                    binding.progressBar.visibility = View.GONE
                    binding.rvHeadline.visibility = View.VISIBLE
                    val adapterHeadlines = HeadlineAdapter(response)
                    binding.rvHeadline.adapter = adapterHeadlines

                    binding.rvHeadline.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            if (dx>0){
                                val lm = recyclerView.layoutManager as LinearLayoutManager
                                val vItem = lm.childCount
                                val lItem = lm.findFirstVisibleItemPosition()
                                val count = adapterHeadlines.itemCount

                                if (!isLoading) {
                                    if (vItem + lItem >= count) {
                                        addMoreData()
                                    }
                                }
                            }
                            super.onScrolled(recyclerView, dx, dy)
                        }
                    })
                } else {
                    adapterHeadline.addData(response)
                    adapterHeadline.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(context, "error ya", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getHeadlineNews(page)

        viewModel.allNewsItem.observe(requireActivity()){ response ->
            if (response != null){
                if (!::adapterAllNews.isInitialized){
                    binding.progressBar2.visibility = View.GONE
                    binding.rvSemuaBerita.visibility = View.VISIBLE
                    val adapterAllNewss = AllNewsAdapter(response)
                    binding.rvSemuaBerita.adapter = adapterAllNewss
                } else {
                    adapterAllNews.addData(response)
                    adapterAllNews.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(context, "error ya", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getAllNews(page)

        binding.tvAllNewsLihatSemua.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("AllNews", "AllNews")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnBusiness.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("AllNews", "business")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnEconomy.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "economy")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnEntertainment.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "entertainment")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnTechnology.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "technology")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnHealth.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "health")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnSport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "sport")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }

        binding.btnGeneral.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("AllNews", "general")
            startActivity(Intent(requireActivity(), AllNewsActivity::class.java).putExtras(bundle))
        }
    }

    private fun addMoreData() {
        binding.progressBarPagination.visibility =  View.VISIBLE
        isLoading = true
        viewModel.loadMoreHeadlineNews()
        isLoading = false
        binding.progressBarPagination.visibility = View.GONE
    }

}