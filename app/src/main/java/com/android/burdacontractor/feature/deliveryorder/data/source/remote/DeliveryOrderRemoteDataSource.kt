package com.android.burdacontractor.feature.deliveryorder.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AddUpdateDeliveryOrderStepOneResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderWithCountResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailResponse
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
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): Flow<PagingData<AllDeliveryOrder>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                DeliveryOrderPagingSource(
                    deliveryOrderService,
                    token,
                    status.name,
                    dateStart,
                    dateEnd,
                    size,
                    search,
                    createdByOrFor
                )
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
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addDeliveryOrderStepOne(
        token: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody,
    ): Flow<ApiResponse<AddUpdateDeliveryOrderStepOneResponse>> = flow {
        val response =
            deliveryOrderService.addDeliveryOrderStepOne(token, addUpdateDeliveryOrderStepOneBody)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addDeliveryOrderStepTwo(
        token: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val response =
            deliveryOrderService.addDeliveryOrderStepTwo(token, addUpdateDeliveryOrderStepTwoBody)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateDeliveryOrderStepOne(
        token: String,
        id: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody,
    ): Flow<ApiResponse<AddUpdateDeliveryOrderStepOneResponse>> = flow {
        val response =
            deliveryOrderService.updateDeliveryOrderStepOne(
                token,
                id,
                addUpdateDeliveryOrderStepOneBody
            )
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateDeliveryOrderStepTwo(
        token: String,
        id: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val response =
            deliveryOrderService.updateDeliveryOrderStepTwo(
                token,
                id,
                addUpdateDeliveryOrderStepTwoBody
            )
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deleteDeliveryOrder(
        token: String,
        id: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response =
            deliveryOrderService.deleteDeliveryOrder(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deletePreOrder(
        token: String,
        id: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response =
            deliveryOrderService.deletePreOrder(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun sendDeliveryOrder(
        token: String,
        id: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response =
            deliveryOrderService.sendDeliveryOrder(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun markCompleteDeliveryOrder(
        token: String,
        id: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response =
            deliveryOrderService.markCompleteDeliveryOrder(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
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

