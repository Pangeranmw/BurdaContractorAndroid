package com.android.burdacontractor.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetLocationFromAddressResponse(

	@field:SerializedName("GetLocationFromAddressResponse")
	val getLocationFromAddressResponse: List<GetLocationFromAddressResponseItem>
) : Parcelable

@Parcelize
data class GetLocationFromAddressResponseItem(

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("osm_id")
	val osmId: Int,

	@field:SerializedName("licence")
	val licence: String,

	@field:SerializedName("boundingbox")
	val boundingbox: List<String>,

	@field:SerializedName("importance")
	val importance: Double,

	@field:SerializedName("powered_by")
	val poweredBy: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("display_name")
	val displayName: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("class")
	val jsonMemberClass: String,

	@field:SerializedName("place_id")
	val placeId: Int,

	@field:SerializedName("lat")
	val lat: String
) : Parcelable
