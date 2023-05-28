package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CetakPreOrder(
    var kodePo: String? = null,
    var namaMaterial: String? = null,
    var ukuran: String? = null,
    var jumlah: String? = null,
    var satuan: String? = null,
    var keterangan: String? = null,
): Parcelable

