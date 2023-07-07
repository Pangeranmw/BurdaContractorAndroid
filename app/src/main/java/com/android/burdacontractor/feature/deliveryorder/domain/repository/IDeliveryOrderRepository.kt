package com.android.burdacontractor.feature.deliveryorder.domain.repository

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DataAllDeliveryOrderWithCount
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import kotlinx.coroutines.flow.Flow

interface IDeliveryOrderRepository {

    fun getAllDeliveryOrder(
        status: DeliveryOrderStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<AllDeliveryOrder>>

    suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetail>>

    suspend fun getSomeActiveDeliveryOrder(size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCount>>
    suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<AllDeliveryOrder>>>
    suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActive>>

    suspend fun addDeliveryOrder(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun updateDeliveryOrder(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deleteDeliveryOrder(
        deliveryOrderId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}