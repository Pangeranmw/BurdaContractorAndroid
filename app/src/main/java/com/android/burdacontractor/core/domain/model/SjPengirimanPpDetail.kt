package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SjPengirimanPpDetail(
    var sjPengirimanPp: SjPengirimanPp? = null,
    var barangHabisPakai: List<PeminjamanPengembalianBarangHabisPakai?>? = null,
    var barangTidakHabisPakai: List<PeminjamanPengembalianBarangTidakHabisPakai?>? = null,
): Parcelable