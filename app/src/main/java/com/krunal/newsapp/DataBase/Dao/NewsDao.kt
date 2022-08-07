package com.krunal.newsapp.DataBase.Dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krunal.newsapp.DataBase.Entity.NewsEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(list: ArrayList<NewsEntity>)

    @Query("SELECT * FROM News")
    fun getAllNews(): PagingSource<Int, NewsEntity>

    @Query("DELETE FROM News")
    fun clearAll()

    @Query("SELECT * FROM News WHERE title LIKE  '%'|| :searchQuery ||'%'")
    fun searchDatabase(searchQuery :String): LiveData<List<NewsEntity>>
}