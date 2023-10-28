package com.example.adaapaindonesia

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adaapaindonesia.databinding.ItemHeadlineBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HeadlineAdapter(private val listHeadlineNews : List<ArticlesItem>)
    : RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>() {
    class HeadlineViewHolder(private val binding: ItemHeadlineBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(articlesItem: ArticlesItem) {
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
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeadlineAdapter.HeadlineViewHolder {
        val view = ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewHolder(view)
    }
    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        holder.bindData(listHeadlineNews[position])
    }
    override fun getItemCount(): Int = listHeadlineNews.size
}