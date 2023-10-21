package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanSimple
import com.android.burdacontractor.feature.profile.domain.model.UserSimple
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
data class TtdVerification(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("sebagai")
	val sebagai: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("ttd")
	val ttd: String
)

data class SuratJalanDetailItem(

	@field:SerializedName("kendaraan")
	val kendaraan: KendaraanSimple,

	@field:SerializedName("admin_gudang")
	val adminGudang: UserSimple? = null,

	@field:SerializedName("tempat_asal")
	val tempatAsal: TempatSuratJalan? = null,

	@field:SerializedName("ttd_admin")
	val ttdAdmin: TtdVerification? = null,

	@field:SerializedName("ttd_driver")
	val ttdDriver: TtdVerification? = null,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("foto_bukti")
	val fotoBukti: String? = null,

	@field:SerializedName("logistic")
	val logistic: UserSimple,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("tempat_tujuan")
	val tempatTujuan: TempatSuratJalan? = null,

	@field:SerializedName("kode_surat")
	val kodeSurat: String,

	@field:SerializedName("ttd_penanggung_jawab")
	val ttdPenanggungJawab: TtdVerification? = null,

	@field:SerializedName("ttd_penanggung_jawab_peminjam")
	val ttdPenanggungJawabPeminjam: TtdVerification? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("site_manager")
	val siteManager: UserSimple? = null,

	@field:SerializedName("tipe")
	val tipe: String,

	@field:SerializedName("supervisor")
	val supervisor: UserSimple? = null,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("peminjaman")
	val peminjaman: List<BarangTidakHabisPakaiItem>? = null,

	@field:SerializedName("penggunaan")
	val penggunaan: List<BarangHabisPakaiItem>? = null,
)