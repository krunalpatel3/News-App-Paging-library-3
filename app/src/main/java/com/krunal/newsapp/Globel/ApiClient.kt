package com.krunal.newsapp.Globel

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {

    companion object{
        const val BASE_URL =  "https://newsapi.org/"
        private var mainRetrofit: Retrofit? = null
        fun getRetrofitInstance(): Retrofit? {
            if (mainRetrofit == null) {
                mainRetrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL) //                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mainRetrofit
        }

    }

}