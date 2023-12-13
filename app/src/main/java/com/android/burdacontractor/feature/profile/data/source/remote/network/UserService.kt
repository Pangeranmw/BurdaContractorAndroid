package com.android.burdacontractor.feature.profile.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetAllUsersResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetUserByTokenResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.UpdateProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @Multipart
    @POST("user/photo")
    suspend fun uploadPhoto(
        @Header("Authorization") token: String,
        @Part foto: MultipartBody.Part,
    ): UpdateProfileResponse

    @FormUrlEncoded
    @POST("user/update/password")
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
    ): UpdateProfileResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("currentAccessToken")
    suspend fun getUserByToken(
        @Header("Authorization") token: String,
    ): GetUserByTokenResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("filter") role: String? = null,
    ): GetAllUsersResponse

    @FormUrlEncoded
    @POST("user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("no_hp") noHp: String,
    ): UpdateProfileResponse

    @FormUrlEncoded
    @POST("user/update/role")
    suspend fun updateRole(
        @Header("Authorization") token: String,
        @Field("user_id") userId: String,
        @Field("role") role: String,
    ): ErrorMessageResponse
}