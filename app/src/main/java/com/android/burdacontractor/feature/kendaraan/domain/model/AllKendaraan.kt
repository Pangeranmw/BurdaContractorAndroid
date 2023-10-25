package com.android.burdacontractor.feature.kendaraan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllKendaraan(

	@field:SerializedName("merk")
	val merk: String,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("gambar")
	val gambar: String,

	@field:SerializedName("nama_logistic")
	val namaLogistic: String? = null,

	@field:SerializedName("gudang_id")
	val gudangId: String,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("logistic_id")
	val logisticId: String? = null,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("plat_nomor")
	val platNomor: String,

	@field:SerializedName("nama_gudang")
	val namaGudang: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable
