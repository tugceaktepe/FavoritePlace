package com.aktepetugce.favoriteplace.home.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aktepetugce.favoriteplace.core.di.GlideApp
import com.aktepetugce.favoriteplace.core.extension.onClick
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.home.databinding.ItemPlaceRowBinding

class PlaceRecyclerAdapter : ListAdapter<Place, PlaceRecyclerAdapter.PlaceViewHolder>(
    PlaceDiffCallBack()
) {
    inner class PlaceViewHolder(val binding: ItemPlaceRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(place: Place) {
            with(binding) {
                placeItemLayout.onClick {
                    onItemClickListener(place)
                }
                textViewName.text = place.name
                GlideApp.with(binding.root.context)
                    .load(place.imageUrl)
                    .into(imageViewThumbnail)
            }
        }
    }

    private class PlaceDiffCallBack : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding =
            ItemPlaceRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.onBind(place)
    }

    private lateinit var onItemClickListener: (Place) -> Unit
    fun setOnItemClickListener(listener: (Place) -> Unit) {
        onItemClickListener = listener
    }
}
