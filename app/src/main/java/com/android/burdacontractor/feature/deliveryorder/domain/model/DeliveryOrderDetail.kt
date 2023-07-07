package com.android.burdacontractor.feature.deliveryorder.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangTidakHabisPakai
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryOrderDetail(
    val id: String? = null,
    val kendaraanMerk: String?=null,
    val kendaraanJenis: String?=null,
    val kendaraanPlatNomor: String?=null,
    val kendaraanGambar: String?=null,
    val adminGudangNama: String?=null,
    val adminGudangFoto: String?=null,
    val adminGudangNoHp: String?=null,
    val purchasingNama: String?=null,
    val purchasingFoto: String?=null,
    val purchasingNoHp: String?=null,
    val logisticId: String?=null,
    val logisticNama: String?=null,
    val logisticFoto: String?=null,
    val logisticNoHp: String?=null,
    val tempatAsalNama: String?=null,
    val tempatAsalCoordinate: String?=null,
    val tempatAsalFoto: String?=null,
    val tempatAsalAlamat: String?=null,
    val tempatTujuanNama: String?=null,
    val tempatTujuanCoordinate: String?=null,
    val tempatTujuanFoto: String?=null,
    val tempatTujuanAlamat: String?=null,
    val ttd: String,
    val createdAt: Long,
    val updatedAt: Long,
    val tglPengambilan: Long,
    val fotoBukti: String? = null,
    val untukPerhatian: String? = null,
    val perihal: String? = null,
    val preOrder: List<PreOrder>? = null,
): Parcelable