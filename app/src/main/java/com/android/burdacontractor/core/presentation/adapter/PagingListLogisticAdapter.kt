package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.convertDistance
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemLogisticBinding
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic

class PagingListLogisticAdapter(val listener: (AllLogistic) -> Unit) :
    PagingDataAdapter<AllLogistic, PagingListLogisticAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemLogisticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemLogisticBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(logistic: AllLogistic) {
            with(binding) {
                tvNama.text = logistic.nama
                logistic.kendaraan?.let {
                    tvKendaraan.text =
                        itemView.context.getString(
                            R.string.merk_plat_kendaraan,
                            it.merk,
                            it.platNomor
                        )
                } ?: binding.tvKendaraan.setGone()

                if (logistic.doActive > 0)
                    tvDoActive.text =
                        itemView.context.getString(R.string.active_do, logistic.doActive)
                else tvDoActive.setGone()

                if (logistic.sjgpActive > 0)
                    tvSjgpActive.text =
                        itemView.context.getString(R.string.active_sjgp, logistic.sjgpActive)
                else tvSjgpActive.setGone()

                if (logistic.sjppActive > 0)
                    tvSjppActive.text =
                        itemView.context.getString(R.string.active_sjpp, logistic.sjppActive)
                else tvSjppActive.setGone()

                if (logistic.sjpgActive > 0)
                    tvSjpgActive.text =
                        itemView.context.getString(R.string.active_sjpg, logistic.sjpgActive)
                else tvSjpgActive.setGone()

                ivLogistic.setImageFromUrl(logistic.foto, itemView.context)

                cvLogistic.setOnClickListener {
                    listener(logistic)
                }

                logistic.jarak?.let {
                    tvJarak.setVisible()
                    tvJarak.text = itemView.context.getString(
                        R.string.jarak_dari_lokasimu,
                        it.convertDistance()
                    )
                } ?: tvJarak.setGone()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllLogistic>() {
            override fun areItemsTheSame(oldItem: AllLogistic, newItem: AllLogistic): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllLogistic,
                newItem: AllLogistic
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}