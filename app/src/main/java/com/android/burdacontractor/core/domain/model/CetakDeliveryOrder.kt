package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CetakDeliveryOrder(
    var kodeDo: String? = null,
    var tglPengambilan: String? = null,
    var perihal: String? = null,
    var untukPerhatian: String? = null,
    var untukPerusahaan: String? = null,
    var kendaraan: String? = null,
    var platNomor: String? = null,
    var logistic: String? = null,
    var purchasing: String? = null,
    var noHpPurchasing: String? = null,
    var ttdPurchasing: String? = null,
    var preOrder: List<CetakPreOrder>,
): Parcelable

