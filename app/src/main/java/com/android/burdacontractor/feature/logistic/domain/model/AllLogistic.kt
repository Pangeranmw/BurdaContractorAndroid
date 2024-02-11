package com.android.burdacontractor.feature.logistic.domain.model

import android.os.Parcelable
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllLogistic(

    @field:SerializedName("kendaraan")
	val kendaraan: Kendaraan? = null,

    @field:SerializedName("role")
	val role: String,

    @field:SerializedName("no_hp")
    val noHp: String,

    @field:SerializedName("ttd")
    val ttd: String,

    @field:SerializedName("jarak")
    val jarak: Double? = null,

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null,

    @field:SerializedName("do_active")
    val doActive: Int,

    @field:SerializedName("total_data")
    val totalData: Int,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("sjgp_active")
    val sjgpActive: Int,

    @field:SerializedName("sjpp_active")
    val sjppActive: Int,

    @field:SerializedName("sjpg_active")
	val sjpgActive: Int,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("durasi")
	val durasi: Double? = null,

    @field:SerializedName("email")
	val email: String
) : Parcelable
