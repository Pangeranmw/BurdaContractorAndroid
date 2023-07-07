package com.android.burdacontractor.feature.deliveryorder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreOrder(
    var id: String? = null,
    var kodePo: String? = null,
    var namaMaterial: String? = null,
    var satuan: String? = null,
    var keterangan: String? = null,
    var ukuran: String? = null,
    var jumlah: Int? = null,
): Parcelable

