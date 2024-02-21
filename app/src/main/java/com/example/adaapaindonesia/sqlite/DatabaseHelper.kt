package com.example.adaapaindonesia.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "savedNews.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_SAVEDNEWS = """
            CREATE TABLE savedNews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                publishedAt TEXT,
                author TEXT,
                urlToImage TEXT,
                description TEXT,
                source TEXT,
                title TEXT,
                url TEXT,
                content TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_SAVEDNEWS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS savedNews")
        onCreate(db)
    }
}