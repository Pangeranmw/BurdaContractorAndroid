package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.data.source.remote.response.PlacesItem
import com.android.burdacontractor.databinding.ItemFindPlaceBinding

class ListFindPlaceAdapter(
    private val listener: (PlacesItem) -> Unit,
) : ListAdapter<PlacesItem, ListFindPlaceAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFindPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemFindPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlacesItem) {
            with(binding) {
                tvNama.text = place.displayName.text
                tvAlamat.text = place.formattedAddress

                cvFindPlace.setOnClickListener {
                    listener(place)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlacesItem>() {
            override fun areItemsTheSame(
                oldItem: PlacesItem,
                newItem: PlacesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PlacesItem,
                newItem: PlacesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}