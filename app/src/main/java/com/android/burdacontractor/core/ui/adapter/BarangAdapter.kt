package com.android.burdacontractor.core.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.domain.model.Barang
import com.android.burdacontractor.databinding.ItemListTourismBinding

class BarangAdapter  : RecyclerView.Adapter<BarangAdapter.ViewHolder>(){
    private lateinit var binding: ItemListTourismBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangAdapter.ViewHolder {
        binding= ItemListTourismBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: BarangAdapter.ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount()=differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root){
        fun setData(item : Barang){
            binding.apply {
                tvItemSubtitle.text=item.detail.toString()
                tvItemTitle.text = item.nama
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Barang>(){
        override fun areItemsTheSame(oldItem: Barang, newItem: Barang): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Barang, newItem: Barang): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

}
