package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pengembalian(
    var id: String? = null,
    var peminjamanId: String? = null,
    var kodePengembalian: String? = null,
    var peminjaman: Peminjaman? = null,
): Parcelable

