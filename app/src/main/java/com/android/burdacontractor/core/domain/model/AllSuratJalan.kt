package com.android.burdacontractor.core.domain.model

data class AllSuratJalan(
    val id: String? = null,
    val kodeSurat: String? = null,
    val status: String? = null,
    val namaTempatAsal: String? = null,
    val alamatTempatAsal: String? = null,
    val coordinateTempatAsal: String? = null,
    val namaTempatTujuan: String? = null,
    val alamatTempatTujuan: String? = null,
    val coordinateTempatTujuan: String? = null,
    val namaAdminGudang: String? = null,
    val fotoAdminGudang: String? = null,
    val namaDriver: String? = null,
    val fotoDriver: String? = null,
    val namaProjectManager: String? = null,
    val fotoProjectManager: String? = null,
    val namaSupervisor: String? = null,
    val fotoSupervisor: String? = null,
    val updatedAt: Long? = null,
)