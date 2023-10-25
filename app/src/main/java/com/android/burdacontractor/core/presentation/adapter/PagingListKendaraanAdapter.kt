package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StatusKendaraan
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ItemKendaraanBinding
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan

class PagingListKendaraanAdapter(val listener: (AllKendaraan) -> Unit) :
    PagingDataAdapter<AllKendaraan, PagingListKendaraanAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemKendaraanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemKendaraanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kendaraan: AllKendaraan) {
            with(binding) {
                tvNama.text = kendaraan.merk
                tvGudang.text = kendaraan.namaGudang
                tvStatus.text = enumValueToNormal(kendaraan.status)
                tvJenis.text = enumValueToNormal(kendaraan.jenis)
                tvPlatKendaraan.text = kendaraan.platNomor
                cvKendaraan.setOnClickListener {
                    listener(kendaraan)
                }
                binding.ivKendaraan.setImageFromUrl(kendaraan.gambar, itemView.context)
                when (kendaraan.jenis) {
                    JenisKendaraan.MINIBUS.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_minibus,
                        0,
                        0,
                        0
                    )

                    JenisKendaraan.MOBIL.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_car,
                        0,
                        0,
                        0
                    )

                    JenisKendaraan.MOTOR.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_motorcycle,
                        0,
                        0,
                        0
                    )

                    JenisKendaraan.PICKUP.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_truck_pickup,
                        0,
                        0,
                        0
                    )

                    JenisKendaraan.TRONTON.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_truck_tronton,
                        0,
                        0,
                        0
                    )

                    JenisKendaraan.TRUCK.name -> binding.tvJenis.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.vehicle_truck_box,
                        0,
                        0,
                        0
                    )
                }
                when (kendaraan.status) {
                    StatusKendaraan.AJUKAN_PENGEMBALIAN.name -> {
                        binding.tvStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_light_half
                            )
                        )
                        binding.tvStatus.setBackgroundResource(R.drawable.semi_rounded_outline_orange)
                    }

                    StatusKendaraan.TERSEDIA.name -> {
                        binding.tvStatus.setBackgroundResource(R.drawable.semi_rounded_outline_secondary_main)
                        binding.tvStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.secondary_main
                            )
                        )
                    }

                    StatusKendaraan.DIGUNAKAN.name -> {
                        binding.tvStatus.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.gray
                            )
                        )
                        binding.tvStatus.setBackgroundResource(R.drawable.semi_rounded_outline_gray)
                    }
                }
                kendaraan.namaLogistic?.let {
                    tvLogistic.setVisible()
                    tvLogistic.text = kendaraan.namaLogistic
                } ?: tvLogistic.setGone()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllKendaraan>() {
            override fun areItemsTheSame(oldItem: AllKendaraan, newItem: AllKendaraan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AllKendaraan,
                newItem: AllKendaraan
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}