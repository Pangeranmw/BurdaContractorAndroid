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
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
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
        dateStart: String?,
        dateEnd: String?,
        size: Int,
        search: String?,
        createdByOrFor: CreatedByOrFor,
    ): LiveData<PagingData<AllDeliveryOrder>> =
        deliveryOrderRemoteDataSource.getAllDeliveryOrder(
            storageDataSource.getToken(), status, dateStart, dateEnd, size, search, createdByOrFor
        ).asLiveData()

    override suspend fun getDeliveryOrderById(id: String): Flow<Resource<DeliveryOrderDetailItem>> = flow {
        emit(Resource.Loading())
        when (val response =
            deliveryOrderRemoteDataSource.getDeliveryOrderById(storageDataSource.getToken(), id)
                .first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.deliveryOrder
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllDeliveryOrderDalamPerjalananByUser(): Flow<Resource<List<AllDeliveryOrder>>> =
        flow {
                emit(Resource.Loading())
                when (val response =
                    deliveryOrderRemoteDataSource.getAllDeliveryOrderDalamPerjalananByUser(
                        storageDataSource.getToken()
                    ).first()) {
                    is ApiResponse.Empty -> {}
                    is ApiResponse.Success -> {
                        val result = response.data.deliveryOrder!!
                        emit(Resource.Success(result, response.data.message))
                    }

                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getSomeActiveDeliveryOrder(size: Int): Flow<Resource<DataAllDeliveryOrderWithCountItem>> =
        flow {
                emit(Resource.Loading())
                when (val response = deliveryOrderRemoteDataSource.getAllDeliveryOrderWithCount(
                    storageDataSource.getToken(),
                    size
                ).first()) {
                    is ApiResponse.Empty -> {}
                    is ApiResponse.Success -> {
                        val result = response.data.data!!
                        emit(Resource.Success(result, response.data.message))
                    }

                    is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    override suspend fun getCountActiveDeliveryOrder(): Flow<Resource<CountActiveResponse>> = flow{
        emit(Resource.Loading())
        when (val response =
            deliveryOrderRemoteDataSource.getCountActiveDeliveryOrder(storageDataSource.getToken())
                .first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun addDeliveryOrderStepOne(
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.addDeliveryOrderStepOne(
            storageDataSource.getToken(),
            addUpdateDeliveryOrderStepOneBody
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.kodeDo
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch { emit(Resource.Error(it.toString())) }.flowOn(Dispatchers.IO)

    override suspend fun addDeliveryOrderStepTwo(
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.addDeliveryOrderStepTwo(
            storageDataSource.getToken(),
            addUpdateDeliveryOrderStepTwoBody
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.id
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch { emit(Resource.Error(it.message.toString())) }

    override suspend fun updateDeliveryOrderStepOne(
        id: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.updateDeliveryOrderStepOne(
            storageDataSource.getToken(),
            id,
            addUpdateDeliveryOrderStepOneBody
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.kodeDo
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch { emit(Resource.Error(it.toString())) }.flowOn(Dispatchers.IO)

    override suspend fun updateDeliveryOrderStepTwo(
        id: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.updateDeliveryOrderStepTwo(
            storageDataSource.getToken(),
            id,
            addUpdateDeliveryOrderStepTwoBody
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.id
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch { emit(Resource.Error(it.message.toString())) }

    override suspend fun uploadFotoBuktiDeliveryOrder(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>> = flow {
        emit(Resource.Loading())
        when (val response = deliveryOrderRemoteDataSource.uploadFotoBuktiDeliveryOrder(
            storageDataSource.getToken(),
            id,
            fotoBukti
        ).first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data
                emit(Resource.Success(result, response.data.message))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteDeliveryOrder(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = deliveryOrderRemoteDataSource.deleteDeliveryOrder(
                storageDataSource.getToken(),
                id
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun deletePreOrder(preOrderId: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = deliveryOrderRemoteDataSource.deletePreOrder(
                storageDataSource.getToken(),
                preOrderId
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun sendDeliveryOrder(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = deliveryOrderRemoteDataSource.sendDeliveryOrder(
                storageDataSource.getToken(),
                id
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun markCompleteDeliveryOrder(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = deliveryOrderRemoteDataSource.markCompleteDeliveryOrder(
                storageDataSource.getToken(),
                id
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

