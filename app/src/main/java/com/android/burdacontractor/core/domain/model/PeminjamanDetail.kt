package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.PeminjamanDetailStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeminjamanDetail(
    var id: String? = null,
    var barangId: String? = null,
    var peminjamanProyekLainId: String? = null,
    var jumlahSatuan: String? = null,
    var status: PeminjamanDetailStatus? = null,
    var peminjamanId: String? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var peminjamanProyekLain: Peminjaman? = null,
    var peminjaman: Peminjaman? = null,
): Parcelable

