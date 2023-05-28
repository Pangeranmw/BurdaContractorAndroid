package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeminjamanPengembalianBarangTidakHabisPakai(
    var id: String? = null,
    var merk: String? = null,
    var nama: String? = null,
    var jumlahSatuan: String? = null,
    var gambar: String? = null,
): Parcelable