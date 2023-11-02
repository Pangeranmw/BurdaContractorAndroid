package com.android.burdacontractor.feature.logistic.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogisticById(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("no_hp")
	val noHp: String,

	@field:SerializedName("ttd")
	val ttd: String,

	@field:SerializedName("count_do_active")
	val countDoActive: Int,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("count_sjgp_active")
	val countSjgpActive: Int,

	@field:SerializedName("count_sjpp_active")
	val countSjppActive: Int,

	@field:SerializedName("count_sjpg_active")
	val countSjpgActive: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
) : Parcelable
