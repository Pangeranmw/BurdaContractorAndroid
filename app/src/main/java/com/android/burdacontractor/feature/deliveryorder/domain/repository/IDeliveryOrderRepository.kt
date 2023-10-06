package com.android.burdacontractor.feature.deliveryorder.domain.repository

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
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
    ): Flow<PagingData<DeliveryOrderItem>>

    suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetailItem>>

    suspend fun getSomeActiveDeliveryOrder(size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCountItem>>
    suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<DeliveryOrderItem>>>
    suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActiveResponse>>

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