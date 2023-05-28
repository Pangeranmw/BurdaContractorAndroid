package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SjPengirimanGp(
    var id: String? = null,
    var suratJalanId: String? = null,
    var peminjamanId: String? = null,
    var suratJalan: SuratJalan? = null,
    var peminjaman: Peminjaman? = null,
): Parcelable

