package com.krunal.newsapp.Globel

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.krunal.newsapp.DataBase.AppDatabase
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.DataBase.Entity.RemoteEntity
import com.krunal.newsapp.Globel.Utility.Companion.getDateFromTimeFormat
import com.krunal.newsapp.Globel.Utility.Companion.getJsonStrFromObject
import com.krunal.newsapp.Globel.Utility.Companion.isNetworkAvailable
import com.krunal.newsapp.Globel.Utility.Companion.yyyy_MM_dd_T_HHmmss_format
import com.krunal.newsapp.Model.Article
import com.krunal.newsapp.Model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

@OptIn(ExperimentalPagingApi::class)
class NewsDataSource constructor(
    private val db: AppDatabase,
    private val apiService: RetrofitInterface
) : RemoteMediator<Int, NewsEntity>() {

    private val STARTING_PAGE_INDEX = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        Log.e("tag", "page: " + page)
        try {

//            if (isNetworkAvailable(context)) {
//
//            }

            val call = apiService.getAllNews(
                "us", "business", Utility.ApiKey, page
            )

            val articlesList = call.body().let { it?.articles }
            Log.e("tag", "articlesList: " + getJsonStrFromObject(articlesList))

            val endOfList = articlesList?.isEmpty()


            db.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    db.getRemoteKeyDao().clearAll()
                    db.getNewsDao().clearAll()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = endOfList?.let { if (endOfList) null else page + 1 }
                val keys = articlesList?.map {
                    RemoteEntity(it.url ?: "", prevKey, nextKey)
                }
                keys?.let { db.getRemoteKeyDao().insertRemote(it) }

                val finalNewsList: ArrayList<NewsEntity> = ArrayList()
                articlesList?.let {
                    for (article in it) {
                        getDateFromTimeFormat(
                            article.publishedAt,
                            yyyy_MM_dd_T_HHmmss_format
                        )?.let { it1 ->
                            finalNewsList.add(
                                NewsEntity(
                                    null,
                                    article.source?.id,
                                    article.source?.name,
                                    article.author,
                                    article.title,
                                    "",
                                    article.url,
                                    article.urlToImage,
                                    it1,
                                    article.content
                                )
                            )
                        }
                    }
                }

                db.getNewsDao().insertNews(finalNewsList)
            }



            Log.e("tag", "MediatorResult: " + getJsonStrFromObject(articlesList))

            return MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            Log.e("tag", "Exception: " + e.message)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("tag", "HttpException: " + e.message)
            return MediatorResult.Error(e)
        }

    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, NewsEntity>): RemoteEntity? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { news -> db.getRemoteKeyDao().getRemoteKeys(news.url ?: "") }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, NewsEntity>): RemoteEntity? {
        return withContext(Dispatchers.IO) {
            state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { dog -> db.getRemoteKeyDao().getRemoteKeys(dog.url ?: "") }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, NewsEntity>): RemoteEntity? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.url?.let { repId ->
                    db.getRemoteKeyDao().getRemoteKeys(repId)
                }
            }
        }
    }


}