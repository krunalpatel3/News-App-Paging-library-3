package com.krunal.newsapp.DataBase

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krunal.newsapp.DataBase.Dao.NewsDao
import com.krunal.newsapp.DataBase.Dao.RemoteKeyDao
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.DataBase.Entity.RemoteEntity

@Database(entities = [NewsEntity::class
    ,RemoteEntity::class],version = 1
    ,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeyDao(): RemoteKeyDao

    companion object {
        @SuppressLint("StaticFieldLeak")
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = context?.let { buildDatabase(it) }
            }
            return INSTANCE
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val dbname = "News.db"

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, dbname
            ).allowMainThreadQueries()
                .build()
        }
    }


}