package com.aktepetugce.favoriteplace.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aktepetugce.favoriteplace.databinding.ItemPlaceRowBinding
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class PlaceRecyclerAdapter @Inject constructor(
                           val glide : RequestManager
) : ListAdapter<UIPlace, PlaceRecyclerAdapter.PlaceViewHolder>(PlaceDiffCallBack()) {

    inner class PlaceViewHolder(val binding: ItemPlaceRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(place: UIPlace) {
            binding.apply {
                glide.load(place.placeImageUrl).into(placeImageView)
                placeNameTextView.text = place.placeName
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
        val binding = ItemPlaceRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
       holder.onBind(getItem(position))
       holder.binding.placeItemLayout.setOnClickListener {
          onItemClickListener(position)
       }
    }

    private lateinit var onItemClickListener : (Int) -> Unit
    fun setOnItemClickListener(listener : (Int) -> Unit) {
        onItemClickListener = listener
    }
}