package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.convertDistance
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemGudangBinding
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang

class PagingListGudangAdapter(val listener: (AllGudang) -> Unit) :
    PagingDataAdapter<AllGudang, PagingListGudangAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemGudangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemGudangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gudang: AllGudang) {
            with(binding) {
                tvNama.text = gudang.nama
                tvTanggal.text = itemView.context.getTimeDifference(gudang.updatedAt)
                tvAlamat.text = gudang.alamat
                ivGudang.setImageFromUrl(gudang.gambar, itemView.context)
                tvKotaProvinsi.text = itemView.context.getString(
                    R.string.kota_dan_provinsi,
                    enumValueToNormal(gudang.kota), enumValueToNormal(gudang.provinsi)
                )
                cvGudang.setOnClickListener {
                    listener(gudang)
                }
                gudang.jarak?.let {
                    tvJarak.setVisible()
                    tvJarak.text = itemView.context.getString(
                        R.string.jarak_dari_lokasimu,
                        it.convertDistance()
                    )
                } ?: tvJarak.setGone()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllGudang>() {
            override fun areItemsTheSame(oldItem: AllGudang, newItem: AllGudang): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllGudang,
                newItem: AllGudang
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}