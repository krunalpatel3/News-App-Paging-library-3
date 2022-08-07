package com.krunal.newsapp.Model

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class News(
    @SerializedName("status")
    var status: String,
    @SerializedName("totalResults")
    var totalResults: Int,
    @SerializedName("articles")
    var articles: ArrayList<Article>?
) :
    Serializable

data class Article(
    @SerializedName("source")
    var source: Source?,
    @SerializedName("author")
    var author: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("urlToImage")
    var urlToImage: String?,
    @SerializedName("publishedAt")
    var publishedAt: String?,
    @SerializedName("content")
    var content: String?
) : Serializable

data class Source(
    @SerializedName("id")
    @Nullable
    var id: String?,
    @SerializedName("name")
    var name: String?
) : Serializable
