package com.android.burdacontractor.feature.suratjalan.data.source.remote.request

import com.android.burdacontractor.feature.suratjalan.domain.model.AddUpdatePeminjamanPenggunaanSuratJalan
import com.google.gson.annotations.SerializedName

data class AddUpdateSuratJalanBody(
    @SerializedName("logistic_id")
    val logisticId: String,
    @SerializedName("kendaraan_id")
    val kendaraanId: String,
    @SerializedName("proyek_id")
    val proyekId: String,
    @SerializedName("peminjaman")
    val peminjaman: List<AddUpdatePeminjamanPenggunaanSuratJalan>,
    @SerializedName("penggunaan")
    val penggunaan: List<AddUpdatePeminjamanPenggunaanSuratJalan>,
    @SerializedName("tipe")
    val tipe: String,
)
