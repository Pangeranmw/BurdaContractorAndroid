package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangHabisPakai
import com.android.burdacontractor.core.domain.model.PeminjamanPengembalianBarangTidakHabisPakai
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
    val ttdSupervisor: String? = null,
    val ttdSupervisorPeminjam: String? = null,
    val ttdAdmin: String,
    val ttdDriver: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val fotoBukti: String? = null,
    val barangHabisPakai: List<PeminjamanPengembalianBarangHabisPakai>? = null,
    val barangTidakHabisPakai: List<PeminjamanPengembalianBarangTidakHabisPakai>? = null,
): Parcelable