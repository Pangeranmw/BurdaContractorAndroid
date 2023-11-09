package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeminjamanSuratJalan(

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("menangani_user")
	val menanganiUser: MenanganiUser,

	@field:SerializedName("menangani_asal_user")
	val menanganiAsalUser: MenanganiUser? = null,

	@field:SerializedName("asal")
	val asal: TempatSuratJalan,

	@field:SerializedName("barang")
	val barang: List<PeminjamanBarangTidakHabisPakaiItem>,

	@field:SerializedName("total_barang")
	val totalBarang: Int,

	@field:SerializedName("kode")
	val kode: String
) : Parcelable

