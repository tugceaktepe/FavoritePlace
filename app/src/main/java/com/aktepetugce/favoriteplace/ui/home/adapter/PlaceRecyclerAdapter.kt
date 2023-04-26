package com.aktepetugce.favoriteplace.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aktepetugce.favoriteplace.databinding.ItemPlaceRowBinding
import com.aktepetugce.favoriteplace.di.GlideApp
import com.aktepetugce.favoriteplace.domain.model.UIPlace
import com.aktepetugce.favoriteplace.util.extension.onClick

class PlaceRecyclerAdapter : ListAdapter<UIPlace, PlaceRecyclerAdapter.PlaceViewHolder>(PlaceDiffCallBack()) {

    inner class PlaceViewHolder(val binding: ItemPlaceRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(place: UIPlace) {
            binding.apply {
                GlideApp.with(binding.root.context)
                    .load(place.placeImageUrl)
                    .into(imageViewThumbnail)
                textViewName.text = place.placeName
            }
        }
    }

    private class PlaceDiffCallBack : DiffUtil.ItemCallback<UIPlace>() {
        override fun areItemsTheSame(oldItem: UIPlace, newItem: UIPlace): Boolean =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: UIPlace, newItem: UIPlace): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.onBind(getItem(position))
        holder.binding.placeItemLayout.onClick {
            onItemClickListener(position)
        }
    }

    private lateinit var onItemClickListener: (Int) -> Unit
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}
