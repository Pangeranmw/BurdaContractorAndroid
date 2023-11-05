package com.android.burdacontractor.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetLocationFromCoordinateResponse(

//	@field:SerializedName("error")
//	val error: String? = null,
//
//	@field:SerializedName("error")
//	val errorObj: GetLocationFromCoordinateResponseError? = null,

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("osm_id")
	val osmId: Int,

	@field:SerializedName("licence")
	val licence: String,

	@field:SerializedName("boundingbox")
	val boundingbox: List<String>,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("powered_by")
	val poweredBy: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("display_name")
	val displayName: String,

	@field:SerializedName("place_id")
	val placeId: Int,

	@field:SerializedName("lat")
	val lat: String
) : Parcelable

@Parcelize
data class GetLocationFromCoordinateResponseError(
	@field:SerializedName("code")
	val code: Int,
	@field:SerializedName("message")
	val message: String,
) : Parcelable

@Parcelize
data class Address(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("state")
	val state: String
) : Parcelable
