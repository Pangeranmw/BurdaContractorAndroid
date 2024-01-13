package com.android.burdacontractor.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetPlaceByQueryResponse(

    @field:SerializedName("places")
    val places: List<PlacesItem>
) : Parcelable

@Parcelize
data class Location(

    @field:SerializedName("latitude")
    val latitude: Double,

    @field:SerializedName("longitude")
    val longitude: Double
) : Parcelable

@Parcelize
data class DisplayName(

    @field:SerializedName("text")
    val text: String,

    @field:SerializedName("languageCode")
    val languageCode: String
) : Parcelable

@Parcelize
data class PlacesItem(

    @field:SerializedName("formattedAddress")
    val formattedAddress: String,

    @field:SerializedName("displayName")
    val displayName: DisplayName,

    @field:SerializedName("location")
    val location: Location
) : Parcelable
