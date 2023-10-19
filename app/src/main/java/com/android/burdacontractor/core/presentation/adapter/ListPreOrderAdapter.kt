package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ItemPreOrderBinding
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.PreOrderItem

class ListPreOrderAdapter : ListAdapter<PreOrderItem, ListPreOrderAdapter.ListPreOrderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPreOrderViewHolder {
        val binding = ItemPreOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPreOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPreOrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ListPreOrderViewHolder(private val binding: ItemPreOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(preOrder: PreOrderItem) {
            with(binding) {
                tvKeterangan.text = preOrder.keterangan
                tvKodePo.text = preOrder.kodePo
                tvNamaMaterial.text = preOrder.namaMaterial
                tvJumlahSatuan.text = itemView.context.getString(R.string.jumlah_satuan, preOrder.jumlah.toString(), preOrder.satuan)
                tvUkuran.text = preOrder.ukuran
                cvPreOrder.setOnClickListener {
//                    val intent = Intent(it.context, DeliveryOrderDetailActivity::class.java)
//                    intent.putExtra(DeliveryOrderDetailActivity.ID_DELIVERY_ORDER, preOrder.id)
//                    it.context.startActivity(intent)
                }
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreOrderItem>() {
            override fun areItemsTheSame(oldItem: PreOrderItem, newItem: PreOrderItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PreOrderItem, newItem: PreOrderItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}