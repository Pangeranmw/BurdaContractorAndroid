package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryOrder(
    var id: String? = null,
    var kodeDo: String? = null,
    var tglPengambilan: Long? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var status: DeliveryOrderStatus? = null,
    var perihal: String? = null,
    var untukPerhatian: String? = null,
    var fotoBukti: String? = null,
    var perusahaanId: String? = null,
    var purchasingId: String? = null,
    var adminGudangId: String? = null,
    var gudangId: String? = null,
    var logisticId: String? = null,
    var kendaraanId: String? = null,
    var gudang: Gudang? = null,
    var perusahaan: Perusahaan? = null,
    var adminGudang: AdminGudang? = null,
    var kendaraan: Kendaraan? = null,
    var logistic: Logistic? = null,
    var purchasing: Purchasing? = null,
): Parcelable

