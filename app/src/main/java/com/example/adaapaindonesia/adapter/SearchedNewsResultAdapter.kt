package com.example.adaapaindonesia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adaapaindonesia.model.ArticlesItem
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.databinding.ItemSearchedNewsBinding
import com.example.adaapaindonesia.databinding.ItemSemuaBeritaBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SearchedNewsResultAdapter(
    private var searchedNews : List<ArticlesItem>
): RecyclerView.Adapter<SearchedNewsResultAdapter.AllNewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit){
        this.onItemClickListener = listener
    }

    class AllNewsViewHolder(private val binding: ItemSearchedNewsBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(articlesItem: ArticlesItem) {
            Glide.with(binding.ivSearchedNews)
                .load(articlesItem.urlToImage)
                .error(R.drawable.gambar_error)
                .into(binding.ivSearchedNews)

            val jsonDate = articlesItem.publishedAt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)

            try {
                val date = inputFormat.parse(jsonDate!!)
                val formattedDate = outputDateFormat.format(date!!)
                val formattedTime = outputTimeFormat.format(date)
                binding.tvSearchedNewsDate.text = formattedDate
                binding.tvSearchedNewsTime.text = " | $formattedTime"
            } catch (e: Exception) {
                binding.tvSearchedNewsDate.text = articlesItem.publishedAt
                binding.tvSearchedNewsDate.visibility = View.GONE
            }

            binding.tvSearchedNewsTitle.text = articlesItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllNewsViewHolder {
        val view = ItemSearchedNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllNewsViewHolder, position: Int) {
        holder.bindData(searchedNews[position])
        holder.itemView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newData : List<ArticlesItem>){
        searchedNews = searchedNews + newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = searchedNews.size
}