package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.databinding.ItemSuratJalanDeliveryOrderBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan

class ListSuratJalanAdapter(val listener: (AllSuratJalan) -> Unit): PagingDataAdapter<AllSuratJalan, ListSuratJalanAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemSuratJalanDeliveryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if(data!=null){
            holder.bind(data)
            holder.itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    class ListViewHolder(private var binding: ItemSuratJalanDeliveryOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind (itemData: AllSuratJalan) {
            binding.tvNamaAsal.text = itemData.namaTempatAsal
            binding.tvNamaTujuan.text = itemData.namaTempatTujuan
            binding.tvAlamatAsal.text = itemData.alamatTempatAsal
            binding.tvAlamatTujuan.text = itemData.alamatTempatTujuan
            binding.tvKode.text = itemData.kodeSurat

//            Glide.with(itemView.context)
//                .load(itemData.fotoAdminGudang)
//                .into(binding.ivItemImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllSuratJalan>() {
            override fun areItemsTheSame(oldItem: AllSuratJalan, newItem: AllSuratJalan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AllSuratJalan, newItem: AllSuratJalan): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}