package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SjPengirimanPp(
    var id: String? = null,
    var suratJalanId: String? = null,
    var peminjamanAsalId: String? = null,
    var peminjamanTujuanId: String? = null,
    var ttdSupervisorPeminjamId: String? = null,
    var peminjamanAsal: Peminjaman? = null,
    var peminjamanTujuan: Peminjaman? = null,
    var suratJalan: SuratJalan? = null,
    ): Parcelable

