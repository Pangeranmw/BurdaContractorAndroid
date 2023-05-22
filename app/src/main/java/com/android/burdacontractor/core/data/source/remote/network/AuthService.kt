package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @GET("register")
    suspend fun register(
        @Field("nama") nama: String,
        @Field("no_hp") noHp: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @GET("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @GET("login/pin")
    suspend fun loginWithPin(
        @Field("pin") pin: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): ErrorMessageResponse
}