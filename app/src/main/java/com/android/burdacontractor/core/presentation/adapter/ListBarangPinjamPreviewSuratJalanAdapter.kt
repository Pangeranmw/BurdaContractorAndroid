package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.databinding.ItemBarangPinjamPreviewBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanBarangTidakHabisPakaiItem

class ListBarangPinjamPreviewSuratJalanAdapter(
    private val listener: (PeminjamanBarangTidakHabisPakaiItem) -> Unit,
) : ListAdapter<PeminjamanBarangTidakHabisPakaiItem, ListBarangPinjamPreviewSuratJalanAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemBarangPinjamPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemBarangPinjamPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: PeminjamanBarangTidakHabisPakaiItem) {
            with(binding) {
                tvMerk.text = barang.merk
                tvNama.text = barang.nama
                ivBarang.setImageFromUrl(barang.gambar, itemView.context)
                cvBarangPreview.setOnClickListener {
                    listener(barang)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PeminjamanBarangTidakHabisPakaiItem>() {
            override fun areItemsTheSame(
                oldItem: PeminjamanBarangTidakHabisPakaiItem,
                newItem: PeminjamanBarangTidakHabisPakaiItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PeminjamanBarangTidakHabisPakaiItem,
                newItem: PeminjamanBarangTidakHabisPakaiItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}