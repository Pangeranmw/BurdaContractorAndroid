package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SjPengembalian(
    var id: String? = null,
    var suratJalanId: String? = null,
    var pengembalianId: String? = null,
    var pengembalian: Pengembalian? = null,
    var suratJalan: SuratJalan? = null,
): Parcelable