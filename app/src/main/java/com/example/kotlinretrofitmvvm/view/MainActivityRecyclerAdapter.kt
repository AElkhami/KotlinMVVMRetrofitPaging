package com.example.kotlinretrofitmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.kotlinretrofitmvvm.R
import com.example.kotlinretrofitmvvm.data.models.Photo
import com.example.kotlinretrofitmvvm.view.MainActivityRecyclerAdapter.ViewHolder

/**
 * Created by A.Elkhami on 1/5/2020.
 */
class MainActivityRecyclerAdapter:
    PagedListAdapter<Photo, ViewHolder>(photoDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_flickr_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        val photo = getItem(position)

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(photo?.getPhotoUrl())
            .placeholder(circularProgressDrawable)
            .centerCrop()
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.photoView)

        holder.imageTitle.text = photo?.title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoView: ImageView = itemView.findViewById(R.id.itemImageView)
        val imageTitle: TextView = itemView.findViewById(R.id.itemTextView)
    }

    companion object {
        fun photoDiff() = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id.equals(newItem.id)
            }
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }

        }
    }
}