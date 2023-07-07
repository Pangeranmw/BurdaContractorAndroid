package com.android.burdacontractor.feature.deliveryorder.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DeliveryOrderDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("delivery_order")
	val deliveryOrder: DeliveryOrderDetailItem
)
data class PreOrderItem(

	@field:SerializedName("ukuran")
	val ukuran: String,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("kode_po")
	val kodePo: String,

	@field:SerializedName("nama_material")
	val namaMaterial: String,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("keterangan")
	val keterangan: String
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

data class TempatDeliveryOrder(

	@field:SerializedName("coordinate")
	val coordinate: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("alamat")
	val alamat: String
)

data class DeliveryOrderDetailItem(

	@field:SerializedName("kendaraan")
	val kendaraan: Kendaraan,

	@field:SerializedName("admin_gudang")
	val adminGudang: UserDeliveryOrder?=null,

	@field:SerializedName("tempat_asal")
	val tempatAsal: TempatDeliveryOrder?=null,

	@field:SerializedName("ttd")
	val ttd: String,

	@field:SerializedName("untuk_perhatian")
	val untukPerhatian: String,

	@field:SerializedName("perihal")
	val perihal: String,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("foto_bukti")
	val fotoBukti: String? = null,

	@field:SerializedName("logistic")
	val logistic: LogisticDeliveryOrder?=null,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("tgl_pengambilan")
	val tglPengambilan: Long,

	@field:SerializedName("tempat_tujuan")
	val tempatTujuan: TempatDeliveryOrder?=null,

	@field:SerializedName("pre_order")
	val preOrder: List<PreOrderItem>? = null,

	@field:SerializedName("kode_do")
	val kodeDo: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("purchasing")
	val purchasing: UserDeliveryOrder?=null,

	@field:SerializedName("status")
	val status: String,
)

data class UserDeliveryOrder(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("no_hp")
	val noHp: String
)
data class LogisticDeliveryOrder(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("no_hp")
	val noHp: String
)