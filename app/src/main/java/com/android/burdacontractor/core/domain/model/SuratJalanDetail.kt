package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuratJalanDetail(
    val id: String? = null,
    val kendaraanMerk: String?=null,
    val kendaraanJenis: String?=null,
    val kendaraanPlatNomor: String?=null,
    val kendaraanGambar: String?=null,
    val adminGudangNama: String?=null,
    val adminGudangFoto: String?=null,
    val adminGudangNoHp: String?=null,
    val projectManagerNama: String?=null,
    val projectManagerFoto: String?=null,
    val projectManagerNoHp: String?=null,
    val supervisorNama: String?=null,
    val supervisorFoto: String?=null,
    val supervisorNoHp: String?=null,
    val logisticNama: String?=null,
    val logisticFoto: String?=null,
    val logisticNoHp: String?=null,
    val gudangNama: String?=null,
    val gudangCoordinate: String?=null,
    val gudangFoto: String?=null,
    val gudangAlamat: String?=null,
    val proyekAsalNama: String?=null,
    val proyekAsalCoordinate: String?=null,
    val proyekAsalFoto: String?=null,
    val proyekAsalAlamat: String?=null,
    val proyekTujuanNama: String?=null,
    val proyekTujuanCoordinate: String?=null,
    val proyekTujuanFoto: String?=null,
    val proyekTujuanAlamat: String?=null,
    val ttdSupervisor: String? = null,
    val ttdAdmin: String,
    val ttdDriver: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val fotoBukti: String? = null,
    val barangHabisPakai: List<PeminjamanPengembalianBarangHabisPakai>? = null,
    val barangTidakHabisPakai: List<PeminjamanPengembalianBarangTidakHabisPakai>? = null,
): Parcelable