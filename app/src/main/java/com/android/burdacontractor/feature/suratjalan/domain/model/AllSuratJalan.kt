package com.android.burdacontractor.feature.suratjalan.domain.model

import com.google.gson.annotations.SerializedName

data class AllSuratJalan(
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

    @field:SerializedName("id_admin_gudang")
    val idAdminGudang: String? = null,

    @field:SerializedName("foto_driver")
    val fotoDriver: String? = null,

    @field:SerializedName("foto_bukti")
    val fotoBukti: String? = null,

    @field:SerializedName("id_driver")
    val idDriver: String? = null,

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

    @field:SerializedName("id_site_manager")
    val idSiteManager: String? = null,

    @field:SerializedName("foto_site_manager")
    val fotoSiteManager: String? = null,

    @field:SerializedName("kode_surat")
    val kodeSurat: String? = null,

    @field:SerializedName("nama_driver")
    val namaDriver: String? = null,

    @field:SerializedName("untuk_saya")
    val untukSaya: Boolean,

    @field:SerializedName("foto_admin_gudang")
    val fotoAdminGudang: String? = null,

    @field:SerializedName("nama_site_manager")
    val namaSiteManager: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("total_data")
    val totalData: Int? = null,
)