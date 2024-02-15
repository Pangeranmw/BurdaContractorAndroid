package com.android.burdacontractor.feature.auth.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.data.source.remote.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("nama") nama: String,
        @Field("no_hp") noHp: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Field("tipe") tipe: String = "logistic",
    ): LoginResponse

    @FormUrlEncoded
    @POST("forget-password")
    suspend fun forgetPassword(
        @Field("email") email: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("login/pin")
    suspend fun loginWithPin(
        @Field("pin") pin: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String,
        @Field("device_token") deviceToken: String,
        @Field("tipe") tipe: String = "logistic",
    ): ErrorMessageResponse
}