package com.example.adaapaindonesia.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adaapaindonesia.model.ArticlesItem
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.view.WebViewActivity
import com.example.adaapaindonesia.databinding.ItemHeadlineBinding
import com.example.adaapaindonesia.model.ArticlesItemHeadline
import java.text.SimpleDateFormat
import java.util.Locale

class HeadlineAdapter(private var listHeadlineNews : List<ArticlesItemHeadline>)
    : RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>() {
    class HeadlineViewHolder(private val binding: ItemHeadlineBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(articlesItem: ArticlesItemHeadline) {
            Glide.with(binding.ivHeadline).load(articlesItem.urlToImage).error(R.drawable.gambar_error)
                .into(binding.ivHeadline)
            val jsonDate = articlesItem.publishedAt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)
            try {
                val date = inputFormat.parse(jsonDate!!)
                val formattedDate = outputDateFormat.format(date!!)
                val formattedTime = outputTimeFormat.format(date)
                binding.tvHeadlineTgl.text = formattedDate
                binding.tvHeadlineJam.text = " | $formattedTime"
            } catch (e: Exception) {
                binding.tvHeadlineTgl.text = articlesItem.publishedAt
                binding.tvHeadlineJam.visibility = View.GONE
            }
            binding.tvHeadlineTitle.text = articlesItem.title
            binding.tvSourceHeadlinenews.text = articlesItem.source?.name ?: "unknown"

            binding.clItemHeadlines.setOnClickListener {
                binding.root.context.startActivity(Intent(binding.root.context, WebViewActivity::class.java)
                    .putExtra("articlesItem",articlesItem))
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeadlineViewHolder {
        val view = ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewHolder(view)
    }
    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        holder.bindData(listHeadlineNews[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newData : List<ArticlesItemHeadline>){
        listHeadlineNews = listHeadlineNews + newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listHeadlineNews.size
}