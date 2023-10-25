package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.databinding.ItemFilterSelectedBinding

class ListFilterSelectedAdapter(val listener: (FilterSelected) -> Unit) :
    ListAdapter<FilterSelected, ListFilterSelectedAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFilterSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemFilterSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: FilterSelected) {
            with(binding) {
                tvFilter.text = filter.filterName
                btnDelete.setOnClickListener {
                    listener(filter)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilterSelected>() {
            override fun areItemsTheSame(
                oldItem: FilterSelected,
                newItem: FilterSelected
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FilterSelected,
                newItem: FilterSelected
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}