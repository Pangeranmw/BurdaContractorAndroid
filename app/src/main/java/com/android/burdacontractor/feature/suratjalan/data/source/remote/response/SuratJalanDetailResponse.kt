package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SuratJalanDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("surat_jalan")
	val suratJalan: SuratJalanDetailItem
)
data class BarangTidakHabisPakaiItem(

	@field:SerializedName("merk")
	val merk: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("jumlah_satuan")
	val jumlahSatuan: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("gambar")
	val gambar: String
)
data class BarangHabisPakaiItem(

	@field:SerializedName("ukuran")
	val ukuran: String,

	@field:SerializedName("merk")
	val merk: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("jumlah_satuan")
	val jumlahSatuan: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("gambar")
	val gambar: String
)

data class Kendaraan(

	@field:SerializedName("merk")
	val merk: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("plat_nomor")
	val platNomor: String,

	@field:SerializedName("gambar")
	val gambar: String
)

data class TempatSuratJalan(

	@field:SerializedName("coordinate")
	val coordinate: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("alamat")
	val alamat: String
)

data class SuratJalanDetailItem(

	@field:SerializedName("kendaraan")
	val kendaraan: Kendaraan,

	@field:SerializedName("admin_gudang")
	val adminGudang: UserSuratJalan?=null,

	@field:SerializedName("tempat_asal")
	val tempatAsal: TempatSuratJalan?=null,

	@field:SerializedName("ttd_admin")
	val ttdAdmin: String,

	@field:SerializedName("ttd_driver")
	val ttdDriver: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("foto_bukti")
	val fotoBukti: String? = null,

	@field:SerializedName("logistic")
	val logistic: LogisticSuratJalan?=null,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("tempat_tujuan")
	val tempatTujuan: TempatSuratJalan?=null,

	@field:SerializedName("barang_habis_pakai")
	val barangHabisPakai: List<BarangHabisPakaiItem>? = null,

	@field:SerializedName("kode_surat")
	val kodeSurat: String,

	@field:SerializedName("ttd_supervisor")
	val ttdSupervisor: String? = null,

	@field:SerializedName("ttd_supervisor_peminjam")
	val ttdSupervisorPeminjam: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("project_manager")
	val projectManager: UserSuratJalan?=null,

	@field:SerializedName("tipe")
	val tipe: String,

	@field:SerializedName("supervisor")
	val supervisor: UserSuratJalan?=null,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("barang_tidak_habis_pakai")
	val barangTidakHabisPakai: List<BarangTidakHabisPakaiItem>? = null
)

data class UserSuratJalan(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("no_hp")
	val noHp: String
)
data class LogisticSuratJalan(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("no_hp")
	val noHp: String
)