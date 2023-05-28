package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.KondisiBarang
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarangTidakHabisPakai(
    var id : String? = null,
    var nomorSeri: String? = null,
    var kondisi: KondisiBarang? = null,
    var keterangan: String? = null,
    var barangId : String? = null,
    var barang: Barang? = null,
    var peminjamanId: String? = null,
    var peminjaman: Peminjaman? = null,
): Parcelable