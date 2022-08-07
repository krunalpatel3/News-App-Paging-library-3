package com.krunal.newsapp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.krunal.newsapp.DataBase.Entity.NewsEntity
import com.krunal.newsapp.databinding.ItemPostArticleBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    var list: ArrayList<NewsEntity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun AddItems(list: List<NewsEntity?>?) {
        this.list = (list as ArrayList<NewsEntity>?)!!
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemPostArticleBinding = ItemPostArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        );
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = list[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ItemViewHolder(private var binding: ItemPostArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: NewsEntity) {
            binding.apply {
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
                itemView.setOnClickListener {
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