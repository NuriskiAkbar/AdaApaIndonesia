package com.example.adaapaindonesia.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.adaapaindonesia.model.SavedNews

class SavedNewsDatabase(context: Context) {
    private val databaseHelper = DatabaseHelper(context)
    fun Insert(savedNews: SavedNews){

        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("id",savedNews.id)
            put("publishedAt", savedNews.publishedAt)
            put("author", savedNews.author)
            put("urlToImage", savedNews.urlToImage)
            put("description", savedNews.description)
            put("source", savedNews.source)
            put("title", savedNews.title)
            put("url", savedNews.url)
            put("content", savedNews.content)
        }

        db.insert("savedNews", null, values)
        db.close()
    }

    fun delete(id: Int){
        val db = databaseHelper.writableDatabase

        db.delete("savedNews", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Recycle", "Range")
    fun getAllSavedNews(): List<SavedNews>{
        val list = mutableListOf<SavedNews>()
        val db = databaseHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM savedNews", null)

        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val publishedAt = cursor.getString(cursor.getColumnIndex("publishedAt"))
            val author = cursor.getString(cursor.getColumnIndex("author"))
            val urlToImage = cursor.getString(cursor.getColumnIndex("urlToImage"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val source = cursor.getString(cursor.getColumnIndex("source"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val url = cursor.getString(cursor.getColumnIndex("url"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            list.add(SavedNews(id, publishedAt, author, urlToImage, description, source, title, url, content))
        }

        cursor.close()
        db.close()
        return list
    }

}