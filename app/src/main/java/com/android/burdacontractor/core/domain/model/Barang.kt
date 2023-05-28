package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.JenisBarang
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barang(
    var id: String? = null,
    var gambar: String? = null,
    var nama: String? = null,
    var merk: String? = null,
    var jenis: JenisBarang? = null,
    var detail: String? = null,
    var gudangId: String? = null,
    var gudang: Gudang? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
): Parcelable

