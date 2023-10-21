package com.android.burdacontractor.feature.kendaraan.domain.model

import com.google.gson.annotations.SerializedName

data class KendaraanByLogistic(

    @field:SerializedName("id_gudang")
    val idGudang: String,

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("alamat_gudang")
    val alamatGudang: String,

    @field:SerializedName("logistic_id")
    val logisticId: String,

    @field:SerializedName("jenis")
    val jenis: String,

    @field:SerializedName("gambar_gudang")
    val gambarGudang: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("plat_nomor")
    val platNomor: String,

    @field:SerializedName("nama_gudang")
    val namaGudang: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("coordinate_gudang")
    val coordinateGudang: String
)