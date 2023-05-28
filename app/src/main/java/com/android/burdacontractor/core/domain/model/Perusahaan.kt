package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Perusahaan (
    var id: String? = null,
    var nama: String? = null,
    var alamat: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var kota: String? = null,
    var provinsi: String? = null,
    var gambar: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
): Parcelable