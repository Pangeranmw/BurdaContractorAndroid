package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PengembalianDetail(
    var id: String? = null,
    var barangId: String? = null,
    var jumlahSatuan: String? = null,
    var pengembalianId: String? = null,
    var barang: Barang? = null,
    var pengembalian: Pengembalian? = null,
): Parcelable