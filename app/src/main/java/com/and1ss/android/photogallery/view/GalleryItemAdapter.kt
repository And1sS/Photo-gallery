package com.and1ss.android.photogallery.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.and1ss.android.photogallery.R
import com.and1ss.android.photogallery.model.GalleryItem
import com.bumptech.glide.Glide

class GalleryItemAdapter(
    val context: Context,
    val fragmentManager: FragmentManager
) : PagedListAdapter<
        GalleryItem,
        GalleryItemAdapter.GalleryItemViewHolder
        >(diffCallback) {

    val requestManager = Glide.with(context)

    inner class GalleryItemViewHolder(
        var item: GalleryItem? = null,
        itemView: ImageView
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(item: GalleryItem) {
            this.item = item
            requestManager
                .load(item.smallUrl)
                .placeholder(R.drawable.seekbar)
                .centerCrop()
                .into(itemView as ImageView)
        }

        override fun onClick(v: View?) {
            val url = if (item?.originalUrl?.isNotEmpty() == true) {
                item?.originalUrl!!
            } else if (item?.smallUrl?.isNotEmpty() == true) {
                item?.smallUrl!!
            } else ""

            Log.d("Test", "onClick: ${item?.originalUrl}, ${item?.smallUrl}")
            ImageDialog
                .newInstance(
                    url, item?.title ?: ""
                ).show(fragmentManager, ImageDialog.TAG)
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

        return GalleryItemViewHolder(itemView = view).also {
            view.setOnClickListener(it)
        }
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
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
