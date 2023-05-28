package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kendaraan(
    var id: String? = null,
    var platNomor: String? = null,
    var gambar: String? = null,
    var logistiId: String? = null,
    var gudangId: String? = null,
    var merk: String? = null,
    var jenis: JenisKendaraan? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var gudang: Gudang? = null,
    var logistic: Logistic? = null,
): Parcelable

