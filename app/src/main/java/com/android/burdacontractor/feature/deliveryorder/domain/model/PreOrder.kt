package com.android.burdacontractor.feature.deliveryorder.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreOrder(

    @field:SerializedName("ukuran")
    var ukuran: String,

    @field:SerializedName("satuan")
    var satuan: String,

    @field:SerializedName("kode_po")
    var kodePo: String? = null,

    @field:SerializedName("nama_material")
    var namaMaterial: String,

    @field:SerializedName("jumlah")
    var jumlah: Int,

    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("keterangan")
    var keterangan: String? = null,

    @field:SerializedName("delivery_order_id")
    var deliveryOrderId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Long? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Long? = null,
) : Parcelable