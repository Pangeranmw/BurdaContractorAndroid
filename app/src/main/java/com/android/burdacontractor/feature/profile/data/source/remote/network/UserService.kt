package com.android.burdacontractor.feature.profile.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetUserByTokenResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @Multipart
    @POST("user/photo")
    suspend fun uploadPhoto(
        @Header("Authorization") token: String,
        @Part foto: MultipartBody.Part,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @PUT("user/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("user/ttd")
    suspend fun uploadTTD(
        @Header("Authorization") token: String,
        @Part ttd: MultipartBody.Part,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("currentAccessToken")
    suspend fun getUserByToken(
        @Header("Authorization") token: String,
    ): GetUserByTokenResponse

    @FormUrlEncoded
    @POST("user/pin")
    suspend fun addPin(
        @Header("Authorization") token: String,
        @Field("pin") pin: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("user/pin")
    suspend fun changePin(
        @Header("Authorization") token: String,
        @Field("old_pin") oldPin: String,
        @Field("new_pin") newPin: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @PUT("user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("no_hp") noHp: String,
    ): ErrorMessageResponse
}