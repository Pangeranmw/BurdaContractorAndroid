package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AllSuratJalanResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("surat_jalan")
	val suratJalan: List<SuratJalanItem>? = null
)
data class SuratJalanItem(
	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tipe")
	val tipe: String? = null,

	@field:SerializedName("alamat_tempat_tujuan")
	val alamatTempatTujuan: String? = null,

	@field:SerializedName("coordinate_tempat_asal")
	val coordinateTempatAsal: String? = null,

	@field:SerializedName("nama_admin_gudang")
	val namaAdminGudang: String? = null,

	@field:SerializedName("foto_driver")
	val fotoDriver: String? = null,

	@field:SerializedName("coordinate_tempat_tujuan")
	val coordinateTempatTujuan: String? = null,

	@field:SerializedName("alamat_tempat_asal")
	val alamatTempatAsal: String? = null,

	@field:SerializedName("nama_tempat_tujuan")
	val namaTempatTujuan: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Long? = null,

	@field:SerializedName("nama_tempat_asal")
	val namaTempatAsal: String? = null,

	@field:SerializedName("foto_project_manager")
	val fotoProjectManager: String? = null,

	@field:SerializedName("kode_surat")
	val kodeSurat: String? = null,

	@field:SerializedName("nama_driver")
	val namaDriver: String? = null,

	@field:SerializedName("foto_admin_gudang")
	val fotoAdminGudang: String? = null,

	@field:SerializedName("nama_project_manager")
	val namaProjectManager: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("nama_supervisor")
	val namaSupervisor: String? = null,

	@field:SerializedName("foto_supervisor")
	val fotoSupervisor: String? = null,

	@field:SerializedName("nama_supervisor_peminjam")
	val namaSupervisorPeminjam: String? = null,

	@field:SerializedName("foto_supervisor_peminjam")
	val fotoSupervisorPeminjam: String? = null,
)
