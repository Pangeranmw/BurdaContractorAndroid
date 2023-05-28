package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Proyek(
    var namaProyek: String? = null,
    var tglSelesai: Long? = null,
    var kota: String? = null,
    var provinsi: String? = null,
    var client: String? = null,
    var selesai: Boolean? = null,
    var longitude: Double? = null,
): Parcelable

