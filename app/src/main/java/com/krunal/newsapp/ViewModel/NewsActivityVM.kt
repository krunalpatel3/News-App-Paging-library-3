package com.krunal.newsapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.Globel.NewsDataSource
import com.krunal.newsapp.Globel.RetrofitInterface

import kotlinx.coroutines.flow.Flow


class NewsActivityVM constructor(
    private val db: AppDatabase,
    private val apiService: RetrofitInterface,
    ) : ViewModel() {

    @ExperimentalPagingApi
    fun getAllNews(): Flow<PagingData<NewsEntity>> = Pager(
        config = PagingConfig(10, enablePlaceholders = false),
        pagingSourceFactory = {
            db.getNewsDao().getAllNews() },
        remoteMediator = NewsDataSource(db, apiService)
    ).flow.cachedIn(viewModelScope)
}