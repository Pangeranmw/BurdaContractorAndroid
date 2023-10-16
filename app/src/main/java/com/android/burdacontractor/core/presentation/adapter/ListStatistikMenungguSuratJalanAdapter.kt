package com.android.burdacontractor.core.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.databinding.ItemStatistikMenungguSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatistikMenungguSuratJalanItem

class ListStatistikMenungguSuratJalanAdapter : ListAdapter<StatistikMenungguSuratJalanItem, ListStatistikMenungguSuratJalanAdapter.ListDeliveryOrderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDeliveryOrderViewHolder {
        val binding = ItemStatistikMenungguSuratJalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDeliveryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDeliveryOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListDeliveryOrderViewHolder(private val binding: ItemStatistikMenungguSuratJalanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: StatistikMenungguSuratJalanItem) {
            with(binding) {
                tvCountStatistik.text = stat.count.toString()
                tvNamaStatistik.text = stat.title
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StatistikMenungguSuratJalanItem>() {
            override fun areItemsTheSame(oldItem: StatistikMenungguSuratJalanItem, newItem: StatistikMenungguSuratJalanItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: StatistikMenungguSuratJalanItem, newItem: StatistikMenungguSuratJalanItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}