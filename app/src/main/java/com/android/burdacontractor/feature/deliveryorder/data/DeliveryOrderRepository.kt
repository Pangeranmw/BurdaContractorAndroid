package com.android.burdacontractor.feature.deliveryorder.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.DeliveryOrderRemoteDataSource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
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
        search: String?,
        createdByOrFor: CreatedByOrFor,
    ): LiveData<PagingData<AllDeliveryOrder>> =
        deliveryOrderRemoteDataSource.getAllDeliveryOrder(
            storageDataSource.getToken(), status, date_start, date_end, size, search, createdByOrFor
        ).asLiveData()

    override suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetailItem>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getDeliveryOrderById(storageDataSource.getToken(), id).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.deliveryOrder
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<AllDeliveryOrder>>> =
        flow {
            try {
                emit(Resource.Loading())
                when (val response =
                    deliveryOrderRemoteDataSource.getAllDeliveryOrderDalamPerjalananByUser(
                        storageDataSource.getToken()
                    ).first()) {
                    is ApiResponse.Empty -> {}
                    is ApiResponse.Success -> {
                        val result = response.data.deliveryOrder!!
                        emit(Resource.Success(result))
                    }

                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
            } catch (ex: Exception) {
                emit(Resource.Error(ex.message.toString()))
            }
        }

    override suspend fun getSomeActiveDeliveryOrder(size: Int): Flow<Resource<DataAllDeliveryOrderWithCountItem>> =
        flow {
            try {
                emit(Resource.Loading())
                when (val response = deliveryOrderRemoteDataSource.getAllDeliveryOrderWithCount(
                    storageDataSource.getToken(),
                    size
                ).first()) {
                    is ApiResponse.Empty -> {}
                    is ApiResponse.Success -> {
                    val result = response.data.data!!
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
    override suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActiveResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.getCountActiveDeliveryOrder(storageDataSource.getToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun addDeliveryOrderStepOne(
        addDeliveryOrderStepOneBody: AddDeliveryOrderStepOneBody
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.addDeliveryOrderStepOne(
            storageDataSource.getToken(),
            addDeliveryOrderStepOneBody
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.kodeDo
                emit(Resource.Success(result))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch { emit(Resource.Error(it.toString())) }.flowOn(Dispatchers.IO)

    override suspend fun addDeliveryOrderStepTwo(
        addDeliveryOrderStepTwoBody: AddDeliveryOrderStepTwoBody
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            when (val response = deliveryOrderRemoteDataSource.addDeliveryOrderStepTwo(
                storageDataSource.getToken(),
                addDeliveryOrderStepTwoBody
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.id
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun updateDeliveryOrder(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }
    override suspend fun uploadFotoBuktiDeliveryOrder(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = deliveryOrderRemoteDataSource.uploadFotoBuktiDeliveryOrder(storageDataSource.getToken(), id, fotoBukti).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun deleteDeliveryOrder(deliveryOrderId: String): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }
}

