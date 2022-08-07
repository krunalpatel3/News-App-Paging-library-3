package com.krunal.newsapp.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.Globel.RetrofitInterface


class MyViewModelFactory(
    private var db: AppDatabase,
    private  var apiService: RetrofitInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsActivityVM(db,apiService) as T
    }


}