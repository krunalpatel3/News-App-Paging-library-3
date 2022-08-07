package com.krunal.newsapp.Globel

import androidx.annotation.CheckResult
import com.krunal.newsapp.Model.News
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("v2/top-headlines")
    @CheckResult
    suspend fun getAllNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Response<News>

}