package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuratJalan(
    var id: String? = null,
    var kodeSurat: String? = null,
    var ttdAdminId: String? = null,
    var ttdDriverId: String? = null,
    var ttdSupervisorId: String? = null,
    var tglPengambilan: String? = null,
    var tipe: SuratJalanTipe? = null,
    var status: SuratJalanStatus? = null,
    var fotoBukti: String? = null,
    var adminGudangId: String? = null,
    var logisticId: String? = null,
    var kendaraanId: String? = null,
    var kendaran: Kendaraan? = null,
    var logistic: Logistic? = null,
//    var ttdAdmin: TtdSjVerification? = null,
//    var ttdSupervisor: TtdSjVerification? = null,
//    var ttdDriver: TtdSjVerification? = null,
    var adminGudang: AdminGudang? = null,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
): Parcelable