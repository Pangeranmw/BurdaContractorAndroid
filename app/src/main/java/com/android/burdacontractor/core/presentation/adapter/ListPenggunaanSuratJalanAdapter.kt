package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemPenggunaanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanBarangHabisPakaiItem
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan

class ListPenggunaanSuratJalanAdapter(
    private val checkedVisible: Boolean = false,
    private val deleteVisible: Boolean = false,
    private val userId: String,
    private val checkedListData: List<PenggunaanSuratJalan> = emptyList(),
    private val listener: ((PenggunaanSuratJalan) -> Unit)? = null,
    private val deleteListener: ((PenggunaanSuratJalan) -> Unit)? = null,
    private val checkedDataListener: ((PenggunaanSuratJalan, Boolean) -> Unit)? = null,
    private val barangListener: ((PenggunaanBarangHabisPakaiItem) -> Unit)? = null,
) : ListAdapter<PenggunaanSuratJalan, ListPenggunaanSuratJalanAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemPenggunaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemPenggunaanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(penggunaan: PenggunaanSuratJalan) {
            with(binding) {
                tvAlamatAsal.text = penggunaan.asal.alamat
                tvKode.text = penggunaan.kode
                tvNamaAsal.text = penggunaan.asal.nama

                penggunaan.menanganiAsalUser?.let {
                    layoutPemberi.setVisible()
                    tvNamaPemberi.text = it.nama
                    it.foto?.let { foto ->
                        binding.ivPemberi.imageTintMode = null
                        ivPemberi.setImageFromUrl(foto, itemView.context)
                    }
                } ?: layoutPemberi.setGone()

                tvNamaPengaju.text = penggunaan.menanganiUser.nama
                penggunaan.menanganiUser.foto?.let {
                    binding.ivPengaju.imageTintMode = null
                    ivPengaju.setImageFromUrl(it, itemView.context)
                }

                val barangAdapter = ListBarangGunaPreviewSuratJalanAdapter { barang ->
                    barangListener?.let { it(barang) }
                }
                barangAdapter.submitList(penggunaan.barang)
                rvBarang.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                rvBarang.adapter = barangAdapter

                tvTanggal.text = getDateFromMillis(penggunaan.createdAt)

                if (checkedVisible) {
                    appCompatCheckBox.setVisible()
                    if (checkedListData.isNotEmpty()) {
                        if (checkedListData.contains(penggunaan)) appCompatCheckBox.isChecked = true
                    }
                    checkedDataListener?.let { checkedDataListener ->
                        appCompatCheckBox.setOnCheckedChangeListener { _, b ->
                            checkedDataListener(penggunaan, b)
                        }
                    }
                }
                if (deleteVisible) {
                    btnDelete.setVisible()
                    btnDelete.setOnClickListener {
                        deleteListener?.let { it(penggunaan) }
                    }
                }

                if (penggunaan.menanganiUser.userId == userId || penggunaan.menanganiAsalUser?.userId == userId) cvPenggunaan.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.secondary_main)
                else cvPenggunaan.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.input)
                cvPenggunaan.setOnClickListener {
                    listener?.let { it(penggunaan) }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PenggunaanSuratJalan>() {
            override fun areItemsTheSame(
                oldItem: PenggunaanSuratJalan,
                newItem: PenggunaanSuratJalan
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PenggunaanSuratJalan,
                newItem: PenggunaanSuratJalan
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}