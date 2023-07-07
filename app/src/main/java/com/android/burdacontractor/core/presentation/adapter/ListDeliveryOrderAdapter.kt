package com.android.burdacontractor.core.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.databinding.ItemDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderDetailActivity

class ListDeliveryOrderAdapter : RecyclerView.Adapter<ListDeliveryOrderAdapter.ListDeliveryOrderViewHolder>() {
    private val listDo = ArrayList<AllDeliveryOrder>()
    private var role: String? = null
    fun setListDeliveryOrder(listDo: List<AllDeliveryOrder>, role: String) {
        val diffCallback = DeliveryOrderDiffCallback(this.listDo, listDo)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.role = role
        this.listDo.clear()
        this.listDo.addAll(listDo)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDeliveryOrderViewHolder {
        val binding = ItemDeliveryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListDeliveryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDeliveryOrderViewHolder, position: Int) {
        holder.bind(listDo[position])
    }

    override fun getItemCount(): Int {
        return listDo.size
    }

    inner class ListDeliveryOrderViewHolder(private val binding: ItemDeliveryOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sj: AllDeliveryOrder) {
            when(role){
                UserRole.ADMIN_GUDANG.name -> {
                    binding.layoutAdminGudang.setGone()
                }
                UserRole.LOGISTIC.name ->{
                    binding.layoutDriver.setGone()
                }
                UserRole.PURCHASING.name ->{
                    binding.layoutPurchasing.setGone()
                }
            }
            when(sj.status){
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.orange_dark_full))
                }
                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.red))
                }
                DeliveryOrderStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context,R.color.secondary_main))
                }
            }
            with(binding) {
                tvKode.text = sj.kodeDo
                tvTanggal.text = itemView.context.getTimeDifference(sj.updatedAt!!)
                tvAlamatAsal.text = sj.alamatTempatAsal
                tvAlamatTujuan.text = sj.alamatTempatTujuan
                tvNamaPurchasing.text = sj.namaPurchasing
                tvNamaDriver.text = sj.namaDriver
                tvNamaAdminGudang.text = sj.namaAdminGudang
                tvNamaAsal.text = sj.namaTempatAsal
                tvNamaTujuan.text = sj.namaTempatTujuan
                tvStatus.text = enumValueToNormal(sj.status!!)
                cvDeliveryOrder.setOnClickListener {
                    val intent = Intent(it.context, DeliveryOrderDetailActivity::class.java)
                    intent.putExtra(DeliveryOrderDetailActivity.ID_SURAT_JALAN, sj.id)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}

class DeliveryOrderDiffCallback(private val oldDeliveryOrder: List<AllDeliveryOrder>, private val newDeliveryOrder: List<AllDeliveryOrder>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldDeliveryOrder.size
    override fun getNewListSize(): Int = newDeliveryOrder.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDeliveryOrder[oldItemPosition].id == newDeliveryOrder[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDo = oldDeliveryOrder[oldItemPosition]
        val newDo = newDeliveryOrder[newItemPosition]
        return oldDo.kodeDo == newDo.kodeDo && oldDo.alamatTempatAsal == newDo.alamatTempatAsal
    }
}