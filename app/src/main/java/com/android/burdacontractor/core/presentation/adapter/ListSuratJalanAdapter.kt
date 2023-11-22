package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getFirstName
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan

class ListSuratJalanAdapter(
    val role: String,
    val userId: String,
    val listener: (AllSuratJalan) -> Unit
) : ListAdapter<AllSuratJalan, ListSuratJalanAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemSuratJalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListViewHolder(private var binding: ItemSuratJalanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(suratJalan: AllSuratJalan) {
            when (role) {
                UserRole.SITE_MANAGER.name, UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name, UserRole.SUPERVISOR.name -> {
                    if (suratJalan.status == DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name) {
                        when (suratJalan.tipe) {
                            SuratJalanTipe.PENGEMBALIAN.name -> {
                                if (role == UserRole.ADMIN_GUDANG.name || role == UserRole.ADMIN.name) {
                                    suratJalan.ttdPenanggungJawab?.let {
                                        binding.tvKonfirmasiSelesai.setVisible()
                                    } ?: binding.tvKonfirmasiSelesai.setGone()
                                }
                            }

                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name -> {
                                if (role == UserRole.SITE_MANAGER.name || role == UserRole.SUPERVISOR.name) {
                                    suratJalan.ttdPenanggungJawab?.let {
                                        binding.tvKonfirmasiSelesai.setVisible()
                                    } ?: binding.tvKonfirmasiSelesai.setGone()
                                }
                            }

                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name -> {
                                if (role == UserRole.SITE_MANAGER.name || role == UserRole.SUPERVISOR.name) {
                                    suratJalan.fotoBukti?.let {
                                        binding.tvKonfirmasiSelesai.setVisible()
                                    } ?: binding.tvKonfirmasiSelesai.setGone()
                                }
                            }
                        }

                    }
                }
            }
            when (suratJalan.tipe) {
                SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name -> {
                    binding.ivJenis.setImageResource(R.drawable.ic_send_req)
                }

                SuratJalanTipe.PENGEMBALIAN.name -> {
                    binding.ivJenis.setImageResource(R.drawable.ic_return_req)
                }
            }
            when (suratJalan.status) {
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.orange_dark_full
                        )
                    )
                }

                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red
                        )
                    )
                }

                DeliveryOrderStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.secondary_main
                        )
                    )
                }
            }
            with(binding) {
                if (suratJalan.untukSaya) cvSuratJalan.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.secondary_main)
                else cvSuratJalan.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.input)
                tvKode.text = suratJalan.kodeSurat
                tvTanggal.text = itemView.context.getTimeDifference(suratJalan.updatedAt!!)
                tvAlamatAsal.text = suratJalan.alamatTempatAsal
                tvAlamatTujuan.text = suratJalan.alamatTempatTujuan
                tvNamaSiteManager.text = suratJalan.namaSiteManager?.getFirstName()
                tvNamaDriver.text = suratJalan.namaDriver?.getFirstName()
                tvNamaAdminGudang.text = suratJalan.namaAdminGudang?.getFirstName()
                tvNamaAsal.text = suratJalan.namaTempatAsal
                tvNamaTujuan.text = suratJalan.namaTempatTujuan
                tvStatus.text = enumValueToNormal(suratJalan.status!!)
                cvSuratJalan.setOnClickListener {
                    listener(suratJalan)
                }
            }
            if (suratJalan.fotoDriver != null) {
                binding.ivDriver.imageTintMode = null
                binding.ivDriver.setImageFromUrl(suratJalan.fotoDriver, itemView.context)
            }
            if (suratJalan.fotoAdminGudang != null) {
                binding.ivAdminGudang.imageTintMode = null
                binding.ivAdminGudang.setImageFromUrl(suratJalan.fotoAdminGudang, itemView.context)
            }
            if (suratJalan.fotoSiteManager != null) {
                binding.ivSiteManager.imageTintMode = null
                binding.ivSiteManager.setImageFromUrl(suratJalan.fotoSiteManager, itemView.context)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllSuratJalan>() {
            override fun areItemsTheSame(oldItem: AllSuratJalan, newItem: AllSuratJalan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllSuratJalan,
                newItem: AllSuratJalan
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}