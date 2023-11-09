package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
    private val checkedListData: List<PenggunaanSuratJalan> = emptyList(),
    private val listener: (PenggunaanSuratJalan) -> Unit,
    private val deleteListener: (PenggunaanSuratJalan) -> Unit,
    private val checkedDataListener: (PenggunaanSuratJalan, Boolean) -> Unit,
    private val barangListener: (PenggunaanBarangHabisPakaiItem) -> Unit,
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
                val totalBarang = penggunaan.totalBarang - penggunaan.barang.size
                if (totalBarang < 1) tvCountBarang.setGone()
                else tvCountBarang.setVisible()
                tvCountBarang.text = itemView.context.getString(
                    R.string.count_barang,
                    totalBarang
                )

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

                val barangAdapter = ListBarangGunaPreviewSuratJalanAdapter { barangListener(it) }
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
                    appCompatCheckBox.setOnCheckedChangeListener { _, b ->
                        checkedDataListener(penggunaan, b)
                    }
                }
                if (deleteVisible) {
                    btnDelete.setVisible()
                    btnDelete.setOnClickListener {
                        deleteListener(penggunaan)
                    }
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