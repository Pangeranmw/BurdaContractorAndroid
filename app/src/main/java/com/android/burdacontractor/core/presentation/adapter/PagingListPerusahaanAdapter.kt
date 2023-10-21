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
import com.android.burdacontractor.databinding.ItemPerusahaanBinding
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan

class PagingListPerusahaanAdapter(val listener: (AllPerusahaan) -> Unit) :
    PagingDataAdapter<AllPerusahaan, PagingListPerusahaanAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemPerusahaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemPerusahaanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(perusahaan: AllPerusahaan) {
            with(binding) {
                tvNama.text = perusahaan.nama
                tvTanggal.text = itemView.context.getTimeDifference(perusahaan.updatedAt)
                tvAlamat.text = perusahaan.alamat
                ivPerusahaan.setImageFromUrl(perusahaan.gambar, itemView.context)
                tvDoDp.text = itemView.context.getString(
                    R.string.format_do_dalam_perjalanan,
                    perusahaan.doDalamPerjalanan
                )
                tvKotaProvinsi.text = itemView.context.getString(
                    R.string.kota_dan_provinsi,
                    enumValueToNormal(perusahaan.kota), enumValueToNormal(perusahaan.provinsi)
                )
                tvDoMk.text = itemView.context.getString(
                    R.string.format_do_dalam_perjalanan,
                    perusahaan.doMenungguKonfirmasi
                )
                cvPerusahaan.setOnClickListener {
                    listener(perusahaan)
                }
                perusahaan.jarak?.let {
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllPerusahaan>() {
            override fun areItemsTheSame(oldItem: AllPerusahaan, newItem: AllPerusahaan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllPerusahaan,
                newItem: AllPerusahaan
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}