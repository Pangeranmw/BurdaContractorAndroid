package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.PeminjamanStatus
import com.android.burdacontractor.core.domain.model.enums.PeminjamanTipe
import kotlinx.parcelize.Parcelize

@Parcelize
data class Peminjaman(
    var id: String? = null,
    var kodePeminjaman: String? = null,
    var tipe: PeminjamanTipe? = null,
    var status: PeminjamanStatus? = null,
    var menanganiId: String? = null,
    var gudangId: String? = null,
    var tglBerakhir: Long? = null,
    var tglPeminjaman: Long? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var gudang: Gudang? = null,
    var menangani: Menangani? = null,
): Parcelable

