package com.android.burdacontractor.feature.profile.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserByTokenResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UserByTokenItem? = null
)

data class UserByTokenItem(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Long? = null,

	@field:SerializedName("ttd")
	val ttd: String? = null,

	@field:SerializedName("device_token")
	val deviceToken: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String? = null
)
