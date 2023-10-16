package com.android.burdacontractor.feature.deliveryorder.data.source.remote

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetUserByTokenResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.DeliveryOrderPagingSource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderWithCountResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryOrderRemoteDataSource @Inject constructor(
    private val deliveryOrderService: DeliveryOrderService,
) {
    fun getAllDeliveryOrder(
        token: String,
        status: DeliveryOrderStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<DeliveryOrderItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                DeliveryOrderPagingSource(deliveryOrderService, token, status.name, date_start, date_end, size, search)
            }
        ).flow
    }

    suspend fun getAllDeliveryOrderWithCount(
        token: String,
        size: Int = 5,
    ): Flow<ApiResponse<AllDeliveryOrderWithCountResponse>> = flow {
        val response = deliveryOrderService.getSomeActiveDeliveryOrder(token, size)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getCountActiveDeliveryOrder(
        token: String,
    ): Flow<ApiResponse<CountActiveResponse>> = flow {
        val response = deliveryOrderService.getCountActiveDeliveryOrder(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
    suspend fun uploadFotoBuktiDeliveryOrder(
        token: String,
        id: String,
        fotoBukti: File,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val requestBody = id.toRequestBody("text/plain".toMediaType())
        val requestImageFile = fotoBukti.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "foto_bukti",
            fotoBukti.name,
            requestImageFile
        )
        val response = deliveryOrderService.uploadFotoBuktiDeliveryOrder(token, requestBody, multipartBody)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getAllDeliveryOrderDalamPerjalananByUser(
        token: String
    ): Flow<ApiResponse<AllDeliveryOrderResponse>> = flow {
        val response = deliveryOrderService.getAllDeliveryOrderDalamPerjalananByUser(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getDeliveryOrderById(token:String, id: String): Flow<ApiResponse<DeliveryOrderDetailResponse>> = flow{
        val response = deliveryOrderService.getDeliveryOrderById(token, id)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
}

