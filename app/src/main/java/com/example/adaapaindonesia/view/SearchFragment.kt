package com.example.adaapaindonesia.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.adapter.SearchedNewsResultAdapter
import com.example.adaapaindonesia.databinding.FragmentSearchBinding
import com.example.adaapaindonesia.factory.NewsViewModelFactory
import com.example.adaapaindonesia.repository.NewsRepository
import com.example.adaapaindonesia.retrofit.ApiService
import com.example.adaapaindonesia.retrofit.Endpoint
import com.example.adaapaindonesia.viewmodel.NewsViewModel
import retrofit2.create


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapterSearchedNews: SearchedNewsResultAdapter
    var page = 1
    var query = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiService.getInstance().create(Endpoint::class.java)
        val repository = NewsRepository(apiService)
        viewModel = ViewModelProvider(this, NewsViewModelFactory(repository))[NewsViewModel::class.java]

        binding.rvResultSearch.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.searchnewsView
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                binding.searchnewsBar.setText(binding.searchnewsView.text.toString())
                binding.searchnewsView.hide()
                if (binding.searchnewsBar.text.toString().isEmpty()){
                    Log.d("status search bar","masih kosong")
                } else {
                    val queryText = binding.searchnewsBar.text.toString()
                    query = queryText
                    binding.lottieLoadinganimation.visibility = View.VISIBLE
                    binding.rvResultSearch.visibility = View.GONE
                    binding.lottieLoadinganimation.playAnimation()

                    viewModel.searchedNewsItem.observe(requireActivity()) { response ->
                        if (response != null){
                            if (!::adapterSearchedNews.isInitialized){
                                binding.lottieLoadinganimation.pauseAnimation()
                                binding.lottieLoadinganimation.visibility = View.GONE
                                binding.rvResultSearch.visibility = View.VISIBLE
                                val adapterSearchedNews = SearchedNewsResultAdapter(response)
                                binding.rvResultSearch.adapter = adapterSearchedNews
                            } else {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                                adapterSearchedNews.addData(response)
                                adapterSearchedNews.notifyDataSetChanged()
                            }
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    viewModel.getSearchedNews(query = query, page = page)

                }
                return@setOnEditorActionListener true
            }




    }
}