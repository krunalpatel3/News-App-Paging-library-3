package com.krunal.newsapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.Globel.Utility
import com.krunal.newsapp.Globel.Utility.Companion.bindImage
import com.krunal.newsapp.databinding.ItemPostArticleBinding

class NewsAdapter : PagingDataAdapter<NewsEntity, NewsAdapter.NewsVH>(DiffUtils) {

    object DiffUtils : DiffUtil.ItemCallback<NewsEntity>() {
        override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem == newItem
        }

    }


    inner class NewsVH(val binding: ItemPostArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        val binding =
            ItemPostArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return NewsVH(binding)
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.binding.apply {

//

                itemArticleTitle.text = item.title
                itemPostDescription.text = item.description
                itemPostAuthor.text = if (item.author != null) item.author.toString()
                    .ifBlank { "Unknown" } else "Unknown"

                itemArticleImage.load(item.urlToImage) {
                    crossfade(true)
                    crossfade(200)
                    transformations(
                        RoundedCornersTransformation(
                            12f,
                            12f,
                            12f,
                            12f
                        )
                    )
                }


                // on item click
                holder.itemView.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }

    }

    // on item click listener
    private var onItemClickListener: ((NewsEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (NewsEntity) -> Unit) {
        onItemClickListener = listener
    }


}