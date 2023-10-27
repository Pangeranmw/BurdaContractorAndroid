package com.android.burdacontractor.feature.deliveryorder.data.source.remote.request

import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.google.gson.annotations.SerializedName

data class AddDeliveryOrderStepTwoBody(
    @SerializedName("logistic_id")
    val logisticId: String,
    @SerializedName("purchasing_id")
    val purchasingId: String,
    @SerializedName("perusahaan_id")
    val perusahaanId: String,
    @SerializedName("gudang_id")
    val gudangId: String,
    @SerializedName("kendaraan_id")
    val kendaraanId: String,
    @SerializedName("perihal")
    val perihal: String,
    @SerializedName("tgl_pengambilan")
    val tglPengambilan: String,
    @SerializedName("untuk_perhatian")
    val untukPerhatian: String,
    @SerializedName("preorder")
    val preOrder: List<PreOrder>,
)