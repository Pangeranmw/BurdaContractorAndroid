package com.android.burdacontractor.feature.deliveryorder.domain.model

import com.google.gson.annotations.SerializedName

data class AllDeliveryOrder(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("alamat_tempat_tujuan")
    val alamatTempatTujuan: String? = null,

    @field:SerializedName("coordinate_tempat_asal")
    val coordinateTempatAsal: String? = null,

    @field:SerializedName("id_admin_gudang")
    val idAdminGudang: String? = null,

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

    @field:SerializedName("foto_bukti")
    val fotoBukti: String? = null,

    @field:SerializedName("nama_tempat_asal")
    val namaTempatAsal: String? = null,

    @field:SerializedName("id_purchasing")
    val idPurchasing: String? = null,

    @field:SerializedName("foto_purchasing")
    val fotoPurchasing: String? = null,

    @field:SerializedName("kode_do")
    val kodeDo: String? = null,

    @field:SerializedName("id_driver")
    val idDriver: String? = null,

    @field:SerializedName("nama_driver")
    val namaDriver: String? = null,

    @field:SerializedName("foto_admin_gudang")
    val fotoAdminGudang: String? = null,

    @field:SerializedName("nama_purchasing")
    val namaPurchasing: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("total_data")
    val totalData: Int? = null,
)