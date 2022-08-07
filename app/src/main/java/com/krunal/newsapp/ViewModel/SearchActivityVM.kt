package com.krunal.newsapp.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.Globel.DebouncedLiveData
import com.krunal.newsapp.Globel.Repository
import com.krunal.newsapp.Globel.Utility

class SearchActivityVM(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository? = null


    private var searchList: LiveData<ArrayList<NewsEntity>>? = null
    var filterCustomerSearch = MutableLiveData<String>()
    init {
        mRepository= Repository.getInstance(application)
        mRepository?.getAppDatabase(application);
    }


    fun getNewsByTitle() : LiveData<List<NewsEntity>> {
        return Transformations.switchMap(
            DebouncedLiveData(
            filterCustomerSearch, 230)) { input: String ->
             mRepository?.getNewsByTitle(input)!!
        }


    }

}