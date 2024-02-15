package com.android.burdacontractor.core.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getFirstName
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.google.android.material.card.MaterialCardView

class PagingListDOAdapter(
    val role: String,
    val id: String,
    val listener: (AllDeliveryOrder) -> Unit
) : PagingDataAdapter<AllDeliveryOrder, PagingListDOAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemDeliveryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    private fun setCardIsForMe(dataId: String?, cv: MaterialCardView, context: Context) {
        if (id == dataId) cv.strokeColor = ContextCompat.getColor(context, R.color.secondary_main)
        else cv.strokeColor = ContextCompat.getColor(context, R.color.input)
    }

    inner class ListViewHolder(private val binding: ItemDeliveryOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(deliveryOrder: AllDeliveryOrder) {
            when (role) {
                UserRole.PURCHASING.name, UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                    setCardIsForMe(
                        deliveryOrder.idPurchasing,
                        binding.cvDeliveryOrder,
                        itemView.context
                    )
                }
            }
            when (role) {
                UserRole.PURCHASING.name, UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                    if (deliveryOrder.status == DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name) {
                        deliveryOrder.fotoBukti?.let {
                            binding.tvKonfirmasiSelesai.setVisible()
                        } ?: binding.tvKonfirmasiSelesai.setGone()
                    }
                }
            }
            when (deliveryOrder.status) {
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.layoutAdminGudang.setGone()
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange_dark_full))
                }
                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.layoutAdminGudang.setGone()
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                }
                DeliveryOrderStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.secondary_main))
                }
            }
            with(binding) {
                tvKode.text = deliveryOrder.kodeDo
                tvTanggal.text = itemView.context.getTimeDifference(deliveryOrder.updatedAt!!)
                tvAlamatAsal.text = deliveryOrder.alamatTempatAsal
                tvAlamatTujuan.text = deliveryOrder.alamatTempatTujuan
                tvNamaPurchasing.text = deliveryOrder.namaPurchasing?.getFirstName()
                tvRolePurchasing.text = enumValueToNormal(deliveryOrder.rolePurchasing.toString())
                tvNamaDriver.text = deliveryOrder.namaDriver?.getFirstName()
                tvNamaAdminGudang.text = deliveryOrder.namaAdminGudang?.getFirstName()
                tvRoleAdminGudang.text = enumValueToNormal(deliveryOrder.roleAdminGudang.toString())
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllDeliveryOrder>() {
            override fun areItemsTheSame(
                oldItem: AllDeliveryOrder,
                newItem: AllDeliveryOrder
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllDeliveryOrder,
                newItem: AllDeliveryOrder
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}