package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemBarangGunaPreviewBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanBarangHabisPakaiItem

class ListBarangGunaPreviewSuratJalanAdapter(
    private val listener: (PenggunaanBarangHabisPakaiItem) -> Unit,
) : ListAdapter<PenggunaanBarangHabisPakaiItem, ListBarangGunaPreviewSuratJalanAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemBarangGunaPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemBarangGunaPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: PenggunaanBarangHabisPakaiItem) {
            with(binding) {
                tvUkuran.text = barang.ukuran
                tvJumlahSatuan.text = barang.jumlahSatuan
                tvNama.text = barang.nama

                barang.gambar?.let {
                    ivBarang.setVisible()
                    ivBarang.setImageFromUrl(it, itemView.context)
                } ?: ivBarang.setGone()

                cvBarangPreview.setOnClickListener {
                    listener(barang)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PenggunaanBarangHabisPakaiItem>() {
            override fun areItemsTheSame(
                oldItem: PenggunaanBarangHabisPakaiItem,
                newItem: PenggunaanBarangHabisPakaiItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PenggunaanBarangHabisPakaiItem,
                newItem: PenggunaanBarangHabisPakaiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}