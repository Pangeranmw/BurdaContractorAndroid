package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ItemStatistikMenungguSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem

class ListStatistikDoSjAdapter :
    ListAdapter<StatisticCountTitleItem, ListStatistikDoSjAdapter.ListDeliveryOrderViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDeliveryOrderViewHolder {
        val binding = ItemStatistikMenungguSuratJalanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListDeliveryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDeliveryOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListDeliveryOrderViewHolder(private val binding: ItemStatistikMenungguSuratJalanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: StatisticCountTitleItem) {
            with(binding) {
                when (stat.title) {
                    "Menunggu Driver" -> {
                        layoutBackground.setBackgroundResource(R.drawable.semi_rounded_outline_red)
                        tvCountStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red
                            )
                        )
                        tvNamaStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red
                            )
                        )
                    }

                    "Dalam Perjalanan" -> {
                        layoutBackground.setBackgroundResource(R.drawable.semi_rounded_outline_orange)
                        tvCountStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_light_half
                            )
                        )
                        tvNamaStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_light_half
                            )
                        )
                    }

                    "Selesai" -> {
                        layoutBackground.setBackgroundResource(R.drawable.semi_rounded_outline_secondary_main)
                        tvCountStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.secondary_main
                            )
                        )
                        tvNamaStatistik.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.secondary_main
                            )
                        )
                    }
                }
                tvCountStatistik.text = stat.count.toString()
                tvNamaStatistik.text = stat.title
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StatisticCountTitleItem>() {
            override fun areItemsTheSame(
                oldItem: StatisticCountTitleItem,
                newItem: StatisticCountTitleItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StatisticCountTitleItem,
                newItem: StatisticCountTitleItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}