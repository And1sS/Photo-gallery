package com.and1ss.android.photogallery.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and1ss.android.photogallery.R
import com.and1ss.android.photogallery.model.GalleryItem
import com.bumptech.glide.Glide
import java.lang.Exception

class GalleryItemAdapter(
    val context: Context
) : PagedListAdapter<
        GalleryItem,
        GalleryItemAdapter.GalleryItemViewHolder
        >(diffCallback) {

    val requestManager = Glide.with(context)

    inner class GalleryItemViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView) {
        fun bind(url: String) {
            requestManager
                .load(url)
                .placeholder(R.drawable.seekbar)
                .centerCrop()
                .into(itemView as ImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            GalleryItemViewHolder {
        val view = LayoutInflater
            .from(context)
            .inflate(
                R.layout.list_item_gallery,
                parent,
                false
            ) as ImageView

        return GalleryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item.url)
        }
    }

    companion object {
        private object diffCallback : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem)
                    : Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem)
                    : Boolean = oldItem == newItem
        }
    }
}
