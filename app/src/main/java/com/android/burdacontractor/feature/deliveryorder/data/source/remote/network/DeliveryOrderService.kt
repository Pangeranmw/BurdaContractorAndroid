package com.android.burdacontractor.feature.deliveryorder.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AddDeliveryOrderStepOneResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderWithCountResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DeliveryOrderService {
    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("delivery-order/all")
    suspend fun getAllDeliveryOrder(
        @Header("Authorization") token: String,
        @Query("status") status: String,
        @Query("date_start") dateStart: String? = null,
        @Query("date_end") dateEnd: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("for") createdByOrFor: String,
    ): AllDeliveryOrderResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("delivery-order/all/dalam-perjalanan")
    suspend fun getAllDeliveryOrderDalamPerjalananByUser(
        @Header("Authorization") token: String,
    ): AllDeliveryOrderResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("delivery-order/active")
    suspend fun getSomeActiveDeliveryOrder(
        @Header("Authorization") token: String,
        @Query("size") size: Int = 5,
    ): AllDeliveryOrderWithCountResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("delivery-order/active/count")
    suspend fun getCountActiveDeliveryOrder(
        @Header("Authorization") token: String,
    ): CountActiveResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("delivery-order/{id}")
    suspend fun getDeliveryOrderById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DeliveryOrderDetailResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("delivery-order/create")
    suspend fun addDeliveryOrderStepOne(
        @Header("Authorization") token: String,
        @Body addDeliveryOrderStepOneBody: AddDeliveryOrderStepOneBody
    ): AddDeliveryOrderStepOneResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("delivery-order/create/pre-order")
    suspend fun addDeliveryOrderStepTwo(
        @Header("Authorization") token: String,
        @Body addDeliveryOrderStepTwoBody: AddDeliveryOrderStepTwoBody
    ): ErrorMessageWithIdResponse

    @FormUrlEncoded
    @POST("delivery-order")
    suspend fun updateDeliveryOrderPengirimanGp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_id") peminjamanId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @DELETE("delivery-order/{id}")
    suspend fun deleteDeliveryOrder(
        @Header("Authorization") token: String,
        @Path("id") deliveryOrderId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("delivery-order/{id}/send")
    suspend fun sendDeliveryOrder(
        @Header("Authorization") token: String,
        @Path("id") deliveryOrderId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("delivery-order/{id}/mark-complete")
    suspend fun markCompleteDeliveryOrder(
        @Header("Authorization") token: String,
        @Path("id") deliveryOrderId: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("delivery-order/upload-foto")
    suspend fun uploadFotoBuktiDeliveryOrder(
        @Header("Authorization") token: String,
        @Part("id") id: RequestBody,
        @Part foto: MultipartBody.Part,
    ): ErrorMessageResponse
}