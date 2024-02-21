package com.example.adaapaindonesia.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.databinding.ActivityWebViewBinding
import com.example.adaapaindonesia.model.ArticlesItem
import com.example.adaapaindonesia.model.ArticlesItemHeadline
import com.example.adaapaindonesia.model.SavedNews
import com.example.adaapaindonesia.sqlite.SavedNewsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebViewActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityWebViewBinding
    private lateinit var savedNewsDatabase: SavedNewsDatabase
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val bundle = intent.getParcelableExtra<ArticlesItemHeadline>("articlesItem")

        mainBinding.webView.webViewClient = WebViewClient()

        mainBinding.webView.loadUrl(bundle!!.url!!)
        mainBinding.webView.settings.javaScriptEnabled = true
        mainBinding.webView.settings.setSupportZoom(true)

        mainBinding.materialToolbar2.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.iv_save_news -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        savedNewsDatabase = SavedNewsDatabase(mainBinding.root.context)
                        val savedNews = SavedNews(id = 0,
                            publishedAt = bundle.publishedAt ?: "none",
                            author = bundle.author ?: "none",
                            content = bundle.content ?: "none",
                            description = bundle.description ?: "none" ,
                            source = bundle.source!!.name ?: "none",
                            title = bundle.title ?: "none",
                            url = bundle.url ?: "none",
                            urlToImage = bundle.urlToImage ?: "none")
                        savedNewsDatabase.Insert(savedNews)
                        withContext(Dispatchers.Main){
                            Toast.makeText(mainBinding.root.context, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    Log.d("log else","error brooooo")
                    return@setOnMenuItemClickListener true
                }
           }
        }
    }

    override fun onBackPressed(){
        if (mainBinding.webView.canGoBack())
            mainBinding.webView.goBack()
        else
            super.onBackPressed()
    }
}