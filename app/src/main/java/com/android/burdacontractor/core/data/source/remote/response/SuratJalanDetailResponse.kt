package com.android.burdacontractor.core.data.source.remote.response

import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangHabisPakai
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangTidakHabisPakai
import com.android.burdacontractor.core.domain.model.SuratJalanDetail
import com.google.gson.annotations.SerializedName

data class SuratJalanDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("surat_jalan")
	val suratJalan: SuratJalanDetailItem
)
fun SuratJalanDetailItem.sjDetailtoDomain(): SuratJalanDetail{
	return SuratJalanDetail(
		id = this.id,
		kendaraanMerk = this.kendaraan.merk,
		kendaraanJenis = this.kendaraan.jenis,
		kendaraanPlatNomor = this.kendaraan.platNomor,
		kendaraanGambar = this.kendaraan.gambar,
		adminGudangNama = this.adminGudang?.nama,
		adminGudangFoto = this.adminGudang?.foto,
		adminGudangNoHp = this.adminGudang?.noHp,
		projectManagerNama = this.projectManager?.nama,
		projectManagerFoto = this.projectManager?.foto,
		projectManagerNoHp = this.projectManager?.noHp,
		supervisorNama = this.supervisor?.nama,
		supervisorFoto = this.supervisor?.foto,
		supervisorNoHp = this.supervisor?.noHp,
		logisticNama = this.logistic?.nama,
		logisticFoto = this.logistic?.foto,
		logisticNoHp = this.logistic?.noHp,
		gudangNama = this.gudang?.nama,
		gudangCoordinate = this.gudang?.coordinate,
		gudangFoto = this.gudang?.foto,
		gudangAlamat = this.gudang?.alamat,
		proyekAsalNama = this.proyekAsal?.nama,
		proyekAsalCoordinate = this.proyekAsal?.coordinate,
		proyekAsalFoto = this.proyekAsal?.foto,
		proyekAsalAlamat = this.proyekAsal?.alamat,
		proyekTujuanNama = this.proyekTujuan?.nama,
		proyekTujuanCoordinate = this.proyekTujuan?.coordinate,
		proyekTujuanFoto = this.proyekTujuan?.foto,
		proyekTujuanAlamat = this.proyekTujuan?.alamat,
		ttdSupervisor = this.ttdSupervisor,
		ttdAdmin = this.ttdAdmin,
		ttdDriver = this.ttdDriver,
		createdAt = this.createdAt,
		updatedAt = this.updatedAt,
		fotoBukti = this.fotoBukti,
		barangHabisPakai = this.barangHabisPakai?.sjBarangHabisPakaiToDomain(),
		barangTidakHabisPakai = this.barangTidakHabisPakai?.sjBarangTidakHabisPakaiToDomain()
	)
}
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
fun List<BarangHabisPakaiItem>.sjBarangHabisPakaiToDomain(): List<PeminjamanPengembalianBarangHabisPakai>{
	return this.map {
		PeminjamanPengembalianBarangHabisPakai(
			id = it.id, merk = it.merk,
			jumlahSatuan = it.jumlahSatuan,
			nama = it.nama, ukuran = it.ukuran,
			gambar = it.gambar
		)
	}
}
fun List<BarangTidakHabisPakaiItem>.sjBarangTidakHabisPakaiToDomain(): List<PeminjamanPengembalianBarangTidakHabisPakai>{
	return this.map {
		PeminjamanPengembalianBarangTidakHabisPakai(
			id = it.id, merk = it.merk,
			jumlahSatuan = it.jumlahSatuan,
			nama = it.nama,
			gambar = it.gambar
		)
	}
}
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

	@field:SerializedName("gudang")
	val gudang: TempatSuratJalan?=null,

	@field:SerializedName("ttd_admin")
	val ttdAdmin: String,

	@field:SerializedName("ttd_driver")
	val ttdDriver: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("foto_bukti")
	val fotoBukti: String? = null,

	@field:SerializedName("logistic")
	val logistic: UserSuratJalan?=null,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("proyek_tujuan")
	val proyekTujuan: TempatSuratJalan?=null,

	@field:SerializedName("barang_habis_pakai")
	val barangHabisPakai: List<BarangHabisPakaiItem>? = null,

	@field:SerializedName("kode_surat")
	val kodeSurat: String,

	@field:SerializedName("ttd_supervisor")
	val ttdSupervisor: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("project_manager")
	val projectManager: UserSuratJalan?=null,

	@field:SerializedName("proyek_asal")
	val proyekAsal: TempatSuratJalan?=null,

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