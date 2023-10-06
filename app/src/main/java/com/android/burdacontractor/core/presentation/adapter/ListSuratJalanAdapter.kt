package com.android.burdacontractor.core.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getFirstName
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.databinding.ItemSuratJalanBinding
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanDetailActivity
import com.bumptech.glide.Glide

class ListSuratJalanAdapter(val user: UserByTokenItem,val listener: (SuratJalanItem)->Unit) : ListAdapter<SuratJalanItem, ListSuratJalanAdapter.ListSuratJalanViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSuratJalanViewHolder {
        val binding = ItemSuratJalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListSuratJalanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSuratJalanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListSuratJalanViewHolder(private val binding: ItemSuratJalanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sj: SuratJalanItem) {
//            when(user.role){
//                UserRole.ADMIN_GUDANG.name -> {
//                    binding.layoutAdminGudang.setGone()
//                }
//                UserRole.LOGISTIC.name ->{
//                    binding.layoutDriver.setGone()
//                }
//                UserRole.SUPERVISOR.name ->{
//                    binding.layoutMenangani.setGone()
//                }
//                UserRole.SITE_MANAGER.name -> {
//                    binding.layoutProjectManager.setGone()
//                }
//            }
            when(sj.tipe){
                SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name -> {
                    binding.ivJenis.setImageResource(R.drawable.ic_send_req)
                }
                SuratJalanTipe.PENGEMBALIAN.name -> {
                    binding.ivJenis.setImageResource(R.drawable.ic_return_req)
                }
            }
            when(sj.status){
                SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange_dark_full))
                    binding.cvSuratJalan.strokeColor = ContextCompat.getColor(itemView.context,R.color.orange_dark_full)
                }
                SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.red))
                    binding.cvSuratJalan.strokeColor = ContextCompat.getColor(itemView.context,R.color.red)
                }
                SuratJalanStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.secondary_main))
                    binding.cvSuratJalan.strokeColor = ContextCompat.getColor(itemView.context,R.color.secondary_main)
                }
            }
            with(binding) {
                tvKode.text = sj.kodeSurat
                tvTanggal.text = itemView.context.getTimeDifference(sj.updatedAt!!)
                tvAlamatAsal.text = sj.alamatTempatAsal
                tvAlamatTujuan.text = sj.alamatTempatTujuan
                tvNamaSiteManager.text = sj.namaSiteManager?.getFirstName()
                tvNamaDriver.text = sj.namaDriver?.getFirstName()
                tvNamaAdminGudang.text = sj.namaAdminGudang?.getFirstName()
                tvNamaAsal.text = sj.namaTempatAsal
                tvNamaTujuan.text = sj.namaTempatTujuan
                tvStatus.text = enumValueToNormal(sj.status!!)
                cvSuratJalan.setOnClickListener {
                    listener(sj)
                }
            }
            if(sj.fotoDriver !=null){
                binding.ivDriver.imageTintMode = null
                Glide.with(itemView.context)
                    .load(getPhotoUrl(sj.fotoDriver))
                    .into(binding.ivDriver)
            }
            if(sj.fotoAdminGudang !=null){
                binding.ivAdminGudang.imageTintMode = null
                Glide.with(itemView.context)
                    .load(getPhotoUrl(sj.fotoAdminGudang))
                    .into(binding.ivAdminGudang)
            }
            if(sj.fotoSiteManager !=null){
                binding.ivSiteManager.imageTintMode = null
                Glide.with(itemView.context)
                    .load(getPhotoUrl(sj.fotoSiteManager))
                    .into(binding.ivSiteManager)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SuratJalanItem>() {
            override fun areItemsTheSame(oldItem: SuratJalanItem, newItem: SuratJalanItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: SuratJalanItem, newItem: SuratJalanItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}