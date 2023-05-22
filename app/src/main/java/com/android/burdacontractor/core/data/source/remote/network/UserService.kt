package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response2.ListTourismResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun addTTD(
        @Header("Authorization") token: String,
        @Part ttd: MultipartBody.Part,
    ): ErrorMessageResponse


    @FormUrlEncoded
    @GET("user/ttd")
    suspend fun getTTD(
        @Header("Authorization") token: String,
        @Field("ttd") ttd: String,
    ): ErrorMessageResponse

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