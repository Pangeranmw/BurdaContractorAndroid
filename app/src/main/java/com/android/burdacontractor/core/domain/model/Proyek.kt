package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Proyek(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("tgl_selesai")
	val tglSelesai: Long,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("site_manager_id")
	val siteManagerId: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("selesai")
	val selesai: Int,

	@field:SerializedName("client")
	val client: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama_proyek")
	val namaProyek: String,

	@field:SerializedName("longitude")
	val longitude: Double
) : Parcelable
