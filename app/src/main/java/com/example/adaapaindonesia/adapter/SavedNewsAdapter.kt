package com.example.adaapaindonesia.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adaapaindonesia.model.SavedNews
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.databinding.ItemSavedNewsBinding
import com.example.adaapaindonesia.databinding.ItemSearchedNewsBinding
import com.example.adaapaindonesia.databinding.ItemSemuaBeritaBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SavedNewsAdapter(
    private var savedNews : MutableList<SavedNews>
): RecyclerView.Adapter<SavedNewsAdapter.AllNewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit){
        this.onItemClickListener = listener
    }

    class AllNewsViewHolder(private val binding: ItemSavedNewsBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(articlesItem: SavedNews) {
            Glide.with(binding.ivSavedNews)
                .load(articlesItem.urlToImage)
                .error(R.drawable.gambar_error)
                .into(binding.ivSavedNews)

            val jsonDate = articlesItem.publishedAt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)

            try {
                val date = inputFormat.parse(jsonDate)
                val formattedDate = outputDateFormat.format(date!!)
                val formattedTime = outputTimeFormat.format(date)
                binding.tvSavedNewsDate.text = formattedDate
                binding.tvSavedNewsTime.text = " | $formattedTime"
            } catch (e: Exception) {
                binding.tvSavedNewsDate.text = articlesItem.publishedAt
                binding.tvSavedNewsDate.visibility = View.GONE
            }

            binding.tvSavedNewsTitle.text = articlesItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllNewsViewHolder {
        val view = ItemSavedNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllNewsViewHolder, position: Int) {
        holder.bindData(savedNews[position])
        holder.itemView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = savedNews.size
    fun deleteItem(adapterPosition: Int) {
        if (adapterPosition in 0 until savedNews.size){
            val deletedItem = savedNews[adapterPosition]
            savedNews.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }
}