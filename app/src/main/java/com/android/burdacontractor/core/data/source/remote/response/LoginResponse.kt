package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

//data class LoginResponse(
//
//	@field:SerializedName("error")
//	val error: Boolean,
//
//	@field:SerializedName("message")
//	val message: String,
//
//	@field:SerializedName("loginResult")
//	val user: LoginResult
//)
//data class LoginResult(
//
//	@field:SerializedName("userId")
//	val userId: String,
//
//	@field:SerializedName("name")
//	val name: String,
//
//	@field:SerializedName("user")
//	val token: String
//)

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: LoginItem? = null
)

data class LoginItem(

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
	val email: String,

	@field:SerializedName("token")
	val token: String
)
