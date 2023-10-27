package com.android.burdacontractor.feature.deliveryorder.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.TempatSimple
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanSimple
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryOrderDetailItem(

    @field:SerializedName("kendaraan")
    val kendaraan: KendaraanSimple,

    @field:SerializedName("admin_gudang")
    val adminGudang: UserDeliveryOrder? = null,

    @field:SerializedName("tempat_asal")
    val tempatAsal: TempatSimple,

    @field:SerializedName("ttd")
    val ttd: String,

    @field:SerializedName("untuk_perhatian")
    val untukPerhatian: String,

    @field:SerializedName("perihal")
    val perihal: String,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("foto_bukti")
    val fotoBukti: String? = null,

    @field:SerializedName("logistic")
    val logistic: UserDeliveryOrder,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("tgl_pengambilan")
    val tglPengambilan: Long,

    @field:SerializedName("tempat_tujuan")
    val tempatTujuan: TempatSimple,

    @field:SerializedName("pre_order")
    val preOrder: List<PreOrder>? = null,

    @field:SerializedName("kode_do")
    val kodeDo: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("purchasing")
    val purchasing: UserDeliveryOrder,

    @field:SerializedName("status")
    val status: String,
) : Parcelable

@Parcelize
data class UserDeliveryOrder(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String,

    @field:SerializedName("no_hp")
    val noHp: String,

    @field:SerializedName("role")
    val role: String
) : Parcelable