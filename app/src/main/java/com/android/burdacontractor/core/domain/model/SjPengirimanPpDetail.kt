package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.data.source.remote.response.Kendaraan
import com.android.burdacontractor.core.data.source.remote.response.TempatSuratJalan
import com.android.burdacontractor.core.data.source.remote.response.UserSuratJalan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SjPengirimanPpDetail(
    var sjPengirimanPp: SjPengirimanPp? = null,
    var barangHabisPakai: List<PeminjamanPengembalianBarangHabisPakai?>? = null,
    var barangTidakHabisPakai: List<PeminjamanPengembalianBarangTidakHabisPakai?>? = null,
): Parcelable