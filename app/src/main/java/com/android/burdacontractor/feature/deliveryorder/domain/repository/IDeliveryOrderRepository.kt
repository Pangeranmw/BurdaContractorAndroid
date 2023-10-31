package com.android.burdacontractor.feature.deliveryorder.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IDeliveryOrderRepository {

    fun getAllDeliveryOrder(
        status: DeliveryOrderStatus,
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): LiveData<PagingData<AllDeliveryOrder>>

    suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetailItem>>

    suspend fun getSomeActiveDeliveryOrder(size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCountItem>>
    suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<AllDeliveryOrder>>>
    suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActiveResponse>>

    suspend fun addDeliveryOrderStepOne(
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ): Flow<Resource<String>>

    suspend fun addDeliveryOrderStepTwo(
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>>

    suspend fun updateDeliveryOrderStepOne(
        id: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ): Flow<Resource<String>>

    suspend fun updateDeliveryOrderStepTwo(
        id: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>>

    suspend fun uploadFotoBuktiDeliveryOrder(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deleteDeliveryOrder(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deletePreOrder(
        preOrderId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun sendDeliveryOrder(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun markCompleteDeliveryOrder(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>
}
