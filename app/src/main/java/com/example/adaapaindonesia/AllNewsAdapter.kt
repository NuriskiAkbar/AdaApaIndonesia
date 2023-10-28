package com.example.adaapaindonesia

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adaapaindonesia.databinding.ItemHeadlineBinding
import com.example.adaapaindonesia.databinding.ItemSemuaBeritaBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AllNewsAdapter(
    private val listHeadlineNews : List<ArticlesItem>
): RecyclerView.Adapter<AllNewsAdapter.AllNewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit){
        this.onItemClickListener = listener
    }

    class AllNewsViewHolder(private val binding: ItemSemuaBeritaBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(articlesItem: ArticlesItem) {
            Glide.with(binding.ivSemuaBerita)
                .load(articlesItem.urlToImage)
                .error(R.drawable.gambar_error)
                .into(binding.ivSemuaBerita)

            val jsonDate = articlesItem.publishedAt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.US)

            try {
                val date = inputFormat.parse(jsonDate!!)
                val formattedDate = outputDateFormat.format(date!!)
                val formattedTime = outputTimeFormat.format(date)
                binding.tvSemuaBeritaTgl.text = formattedDate
                binding.tvSemuaBeritaJam.text = " | $formattedTime"
            } catch (e: Exception) {
                binding.tvSemuaBeritaTgl.text = articlesItem.publishedAt
                binding.tvSemuaBeritaJam.visibility = View.GONE
            }

            binding.tvSemuaBeritaTitle.text = articlesItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllNewsAdapter.AllNewsViewHolder {
        val view = ItemSemuaBeritaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllNewsViewHolder, position: Int) {
        holder.bindData(listHeadlineNews[position])
        holder.itemView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = listHeadlineNews.size
}