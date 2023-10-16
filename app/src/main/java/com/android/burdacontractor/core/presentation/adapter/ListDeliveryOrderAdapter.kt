package com.android.burdacontractor.core.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getFirstName
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.databinding.ItemDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.bumptech.glide.Glide

class ListDeliveryOrderAdapter(val role: String, val id: String, val listener: (DeliveryOrderItem) -> Unit) : ListAdapter<DeliveryOrderItem, ListDeliveryOrderAdapter.ListDeliveryOrderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDeliveryOrderViewHolder {
        val binding = ItemDeliveryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDeliveryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDeliveryOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListDeliveryOrderViewHolder(private val binding: ItemDeliveryOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(deliveryOrder: DeliveryOrderItem) {
            when(role){
                UserRole.ADMIN_GUDANG.name -> {
                    if(id == deliveryOrder.idAdminGudang) {
                        binding.cvDeliveryOrder.strokeColor = ContextCompat.getColor(itemView.context,R.color.secondary_main)
                    }
                }
                UserRole.LOGISTIC.name ->{
//                    binding.layoutDriver.setGone()
                }
                UserRole.PURCHASING.name ->{
                    if(id == deliveryOrder.idPurchasing) {
                        binding.cvDeliveryOrder.strokeColor = ContextCompat.getColor(itemView.context,R.color.secondary_main)
                    }
                }
            }
            when(deliveryOrder.status){
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.layoutAdminGudang.setGone()
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange_dark_full))
                }
                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.layoutAdminGudang.setGone()
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.red))
                }
                DeliveryOrderStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.secondary_main))
                }
            }
            with(binding) {
                tvKode.text = deliveryOrder.kodeDo
                tvTanggal.text = itemView.context.getTimeDifference(deliveryOrder.updatedAt!!)
                tvAlamatAsal.text = deliveryOrder.alamatTempatAsal
                tvAlamatTujuan.text = deliveryOrder.alamatTempatTujuan
                tvNamaPurchasing.text = deliveryOrder.namaPurchasing?.getFirstName()
                tvNamaDriver.text = deliveryOrder.namaDriver?.getFirstName()
                tvNamaAdminGudang.text = deliveryOrder.namaAdminGudang?.getFirstName()
                tvNamaAsal.text = deliveryOrder.namaTempatAsal
                tvNamaTujuan.text = deliveryOrder.namaTempatTujuan
                tvStatus.text = enumValueToNormal(deliveryOrder.status!!)
                cvDeliveryOrder.setOnClickListener {
                    listener(deliveryOrder)
                }
            }
            if(deliveryOrder.fotoDriver !=null){
                binding.ivDriver.imageTintMode = null
                binding.ivDriver.setImageFromUrl(deliveryOrder.fotoDriver,itemView.context)
            }
            if(deliveryOrder.fotoAdminGudang !=null){
                binding.ivAdminGudang.imageTintMode = null
                binding.ivAdminGudang.setImageFromUrl(deliveryOrder.fotoAdminGudang,itemView.context)
            }
            if(deliveryOrder.fotoPurchasing !=null){
                binding.ivPurchasing.imageTintMode = null
                binding.ivPurchasing.setImageFromUrl(deliveryOrder.fotoPurchasing,itemView.context)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DeliveryOrderItem>() {
            override fun areItemsTheSame(oldItem: DeliveryOrderItem, newItem: DeliveryOrderItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DeliveryOrderItem, newItem: DeliveryOrderItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}