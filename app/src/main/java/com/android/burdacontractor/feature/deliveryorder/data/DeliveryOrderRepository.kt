package com.android.burdacontractor.feature.deliveryorder.data

import android.util.Log
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.DeliveryOrderRemoteDataSource
import com.android.burdacontractor.feature.profile.data.source.remote.UserRemoteDataSource
import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DataAllDeliveryOrderWithCount
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryOrderRepository @Inject constructor(
    private val deliveryOrderRemoteDataSource: DeliveryOrderRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IDeliveryOrderRepository {

    override fun getAllDeliveryOrder(
        status: DeliveryOrderStatus,
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?
    ): Flow<PagingData<AllDeliveryOrder>> =
        deliveryOrderRemoteDataSource.getAllDeliveryOrder(
            storageDataSource.getToken(),status,date_start,date_end,size,search
        )

    override suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetail>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getDeliveryOrderById(storageDataSource.getToken(), id).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = DataMapper.deliveryOrderDetailResponsesToDomain(response.data.deliveryOrder)
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
    override suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<AllDeliveryOrder>>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getAllDeliveryOrderDalamPerjalananByUser(storageDataSource.getToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = DataMapper.mapAllDeliveryOrderResponsesToDomain(response.data.deliveryOrder!!)
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
    override suspend fun getSomeActiveDeliveryOrder(size: Int): Flow<Resource<DataAllDeliveryOrderWithCount>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getAllDeliveryOrderWithCount(storageDataSource.getToken(), size).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = DataMapper.dataAllDeliveryOrderWithCountResponsesToDomain(response.data.data!!)
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
    override suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActive>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getCountActiveDeliveryOrder(storageDataSource.getToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = DataMapper.countActiveResponsesToDomain(response.data)
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun addDeliveryOrder(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDeliveryOrder(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDeliveryOrder(deliveryOrderId: String): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }
}

