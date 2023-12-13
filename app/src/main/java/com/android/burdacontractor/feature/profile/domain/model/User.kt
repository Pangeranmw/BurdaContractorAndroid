package com.android.burdacontractor.feature.profile.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String,

	@field:SerializedName("updated_at")
	val updatedAt: Long,

	@field:SerializedName("ttd")
	val ttd: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
) : Parcelable
