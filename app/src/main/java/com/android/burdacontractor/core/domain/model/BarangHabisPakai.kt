package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.KondisiBarang
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarangHabisPakai(
    var id: String? = null,
    var ukuran: String? = null,
    var satuan: String? = null,
    var jumlah: String? = null,
    var kondisi: KondisiBarang? = null,
    var barangId : String? = null,
    var barang: Barang? = null,
): Parcelable