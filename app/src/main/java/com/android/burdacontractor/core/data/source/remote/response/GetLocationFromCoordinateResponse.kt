package com.android.burdacontractor.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetLocationFromCoordinateResponse(

	@field:SerializedName("osm_id")
	val osmId: Int,

	@field:SerializedName("place_rank")
	val placeRank: Int,

	@field:SerializedName("licence")
	val licence: String,

	@field:SerializedName("boundingbox")
	val boundingbox: List<String>,

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("importance")
	val importance: Double,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("display_name")
	val displayName: String,

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("addresstype")
	val addresstype: String,

	@field:SerializedName("class")
	val jsonMemberClass: String,

	@field:SerializedName("place_id")
	val placeId: Int,

	@field:SerializedName("lat")
	val lat: String
) : Parcelable

@Parcelize
data class Address(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("ISO3166-2-lvl3")
	val iSO31662Lvl3: String,

	@field:SerializedName("ISO3166-2-lvl4")
	val iSO31662Lvl4: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("suburb")
	val suburb: String,

	@field:SerializedName("village")
	val village: String,

	@field:SerializedName("city_district")
	val cityDistrict: String,

	@field:SerializedName("region")
	val region: String,

	@field:SerializedName("city_block")
	val cityBlock: String
) : Parcelable

