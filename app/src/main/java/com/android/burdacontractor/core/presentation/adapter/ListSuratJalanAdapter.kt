package com.android.burdacontractor.core.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.databinding.ItemSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanDetailActivity
import com.bumptech.glide.Glide

class ListSuratJalanAdapter : RecyclerView.Adapter<ListSuratJalanAdapter.ListSuratJalanViewHolder>() {
    private val listSj = ArrayList<AllSuratJalan>()
    private var role: String? = null
    fun setListSuratJalan(listSj: List<AllSuratJalan>, role: String) {
        val diffCallback = SuratJalanDiffCallback(this.listSj, listSj)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.role = role
        this.listSj.clear()
        this.listSj.addAll(listSj)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSuratJalanViewHolder {
        val binding = ItemSuratJalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListSuratJalanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSuratJalanViewHolder, position: Int) {
        holder.bind(listSj[position])
    }

    override fun getItemCount(): Int {
        return listSj.size
    }

    inner class ListSuratJalanViewHolder(private val binding: ItemSuratJalanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sj: AllSuratJalan) {
            when(role){
                UserRole.ADMIN_GUDANG.name -> {
                    binding.layoutAdminGudang.setGone()
                }
                UserRole.LOGISTIC.name ->{
                    binding.layoutDriver.setGone()
                }
                UserRole.SUPERVISOR.name ->{
                    binding.layoutSupervisor.setGone()
                }
                UserRole.PROJECT_MANAGER.name -> {
                    binding.layoutProjectManager.setGone()
                }
            }
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
                tvNamaProjectManager.text = sj.namaProjectManager
                tvNamaSupervisor.text = sj.namaSupervisor
                tvNamaDriver.text = sj.namaDriver
                tvNamaAdminGudang.text = sj.namaAdminGudang
                tvNamaAsal.text = sj.namaTempatAsal
                tvNamaTujuan.text = sj.namaTempatTujuan
                tvStatus.text = enumValueToNormal(sj.status!!)
                cvSuratJalan.setOnClickListener {
                    val intent = Intent(it.context, SuratJalanDetailActivity::class.java)
                    intent.putExtra(SuratJalanDetailActivity.ID_SURAT_JALAN, sj.id)
                    it.context.startActivity(intent)
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
            if(sj.fotoProjectManager !=null){
                binding.ivProjectManager.imageTintMode = null
                Glide.with(itemView.context)
                    .load(getPhotoUrl(sj.fotoProjectManager))
                    .into(binding.ivProjectManager)
            }
            if(sj.fotoSupervisor !=null){
                binding.ivSupervisor.imageTintMode = null
                Glide.with(itemView.context)
                    .load(getPhotoUrl(sj.fotoSupervisor))
                    .into(binding.ivSupervisor)
            }
        }
    }
}

class SuratJalanDiffCallback(private val oldSuratJalan: List<AllSuratJalan>, private val newSuratJalan: List<AllSuratJalan>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldSuratJalan.size
    override fun getNewListSize(): Int = newSuratJalan.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSuratJalan[oldItemPosition].id == newSuratJalan[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSj = oldSuratJalan[oldItemPosition]
        val newSj = newSuratJalan[newItemPosition]
        return oldSj.kodeSurat == newSj.kodeSurat && oldSj.alamatTempatAsal == newSj.alamatTempatAsal
    }
}