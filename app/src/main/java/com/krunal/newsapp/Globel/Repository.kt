package com.krunal.newsapp.Globel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.DataBase.Entity.NewsEntity

class Repository {

    var context: Context? = null
    var mAppDatabase: AppDatabase? = null

    private constructor(application :Context){
        context = application
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mInstance: Repository? = null

        /**
         * Get default instance of the class to keep it a singleton
         */
        fun getInstance(application: Context): Repository? {
            if (mInstance == null) {
               mInstance = Repository(application)
            }
            return mInstance
        }
    }

    fun getAppDatabase(context: Context?) {
        mAppDatabase = AppDatabase.getInstance(context)
    }

    fun getNewsByTitle(title:String) : LiveData<List<NewsEntity>>? {
       return mAppDatabase?.getNewsDao()?.searchDatabase(title)
    }



}