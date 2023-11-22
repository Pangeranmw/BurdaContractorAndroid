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
import com.android.burdacontractor.databinding.ItemPeminjamanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanBarangTidakHabisPakaiItem
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan

class ListPeminjamanSuratJalanAdapter(
    private val checkedVisible: Boolean = false,
    private val deleteVisible: Boolean = false,
    private val checkedListData: List<PeminjamanSuratJalan> = emptyList(),
    private val listener: (PeminjamanSuratJalan) -> Unit,
    private val deleteListener: (PeminjamanSuratJalan) -> Unit,
    private val checkedDataListener: (PeminjamanSuratJalan, Boolean) -> Unit,
    private val barangListener: (PeminjamanBarangTidakHabisPakaiItem) -> Unit,
    private val userId: String,
) : ListAdapter<PeminjamanSuratJalan, ListPeminjamanSuratJalanAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemPeminjamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private val binding: ItemPeminjamanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(peminjaman: PeminjamanSuratJalan) {
            with(binding) {
                tvAlamatAsal.text = peminjaman.asal.alamat
                tvKode.text = peminjaman.kode
                tvNamaAsal.text = peminjaman.asal.nama

                peminjaman.menanganiAsalUser?.let {
                    layoutPemberi.setVisible()
                    tvNamaPemberi.text = it.nama
                    it.foto?.let { foto ->
                        binding.ivPemberi.imageTintMode = null
                        ivPemberi.setImageFromUrl(foto, itemView.context)
                    }
                } ?: layoutPemberi.setGone()

                tvNamaPengaju.text = peminjaman.menanganiUser.nama
                peminjaman.menanganiUser.foto?.let {
                    binding.ivPengaju.imageTintMode = null
                    ivPengaju.setImageFromUrl(it, itemView.context)
                }

                if (peminjaman.menanganiUser.userId == userId || peminjaman.menanganiAsalUser?.userId == userId) cvPeminjaman.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.secondary_main)
                else cvPeminjaman.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.input)
                cvPeminjaman.setOnClickListener {
                    listener(peminjaman)
                }

                val barangAdapter = ListBarangPinjamPreviewSuratJalanAdapter { barangListener(it) }
                barangAdapter.submitList(peminjaman.barang)
                rvBarang.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                rvBarang.adapter = barangAdapter

                if (checkedVisible) {
                    appCompatCheckBox.setVisible()
                    if (checkedListData.isNotEmpty()) {
                        if (checkedListData.contains(peminjaman)) appCompatCheckBox.isChecked = true
                    }
                    appCompatCheckBox.setOnCheckedChangeListener { _, b ->
                        checkedDataListener(peminjaman, b)
                    }
                }
                tvTanggal.text = getDateFromMillis(peminjaman.createdAt)
                if (deleteVisible) {
                    btnDelete.setVisible()
                    btnDelete.setOnClickListener {
                        deleteListener(peminjaman)
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PeminjamanSuratJalan>() {
            override fun areItemsTheSame(
                oldItem: PeminjamanSuratJalan,
                newItem: PeminjamanSuratJalan
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PeminjamanSuratJalan,
                newItem: PeminjamanSuratJalan
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}