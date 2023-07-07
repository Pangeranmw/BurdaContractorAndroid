package com.android.burdacontractor.feature.deliveryorder.domain.model

data class AllDeliveryOrder(
    val id: String? = null,
    val alamatTempatTujuan: String? = null,
    val coordinateTempatAsal: String? = null,
    val namaAdminGudang: String? = null,
    val fotoDriver: String? = null,
    val coordinateTempatTujuan: String? = null,
    val alamatTempatAsal: String? = null,
    val namaTempatTujuan: String? = null,
    val updatedAt: Long? = null,
    val namaTempatAsal: String? = null,
    val fotoPurchasing: String? = null,
    val kodeDo: String? = null,
    val namaDriver: String? = null,
    val fotoAdminGudang: String? = null,
    val namaPurchasing: String? = null,
    val status: String? = null,
)