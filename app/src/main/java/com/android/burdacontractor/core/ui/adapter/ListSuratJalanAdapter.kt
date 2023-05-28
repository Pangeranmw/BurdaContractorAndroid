package com.android.burdacontractor.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.AllSuratJalan
import com.android.burdacontractor.databinding.ItemListTourismBinding
import com.bumptech.glide.Glide

class ListSuratJalanAdapter: PagingDataAdapter<AllSuratJalan, ListSuratJalanAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if(data!=null){
            holder.bind(data)
        }
        holder.itemView.setOnClickListener {
//            val intent = Intent(it.context, DetailActivity::class.java)
//            intent.putExtra(DetailActivity.EXTRA_STORY, data)
//            it.context.startActivity(intent,
//                ActivityOptionsCompat
//                    .makeSceneTransitionAnimation(it.context as Activity)
//                    .toBundle()
//            )
        }
    }

    class ListViewHolder(private var binding: ItemListTourismBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind (itemData: AllSuratJalan) {
            binding.tvItemSubtitle.text = itemData.kodeSurat
            Glide.with(itemView.context)
                .load(itemData.fotoAdminGudang)
                .into(binding.ivItemImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllSuratJalan>() {
            override fun areItemsTheSame(oldItem: AllSuratJalan, newItem: AllSuratJalan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AllSuratJalan, newItem: AllSuratJalan): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}