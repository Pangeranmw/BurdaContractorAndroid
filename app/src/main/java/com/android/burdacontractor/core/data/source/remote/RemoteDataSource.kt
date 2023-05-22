package com.android.burdacontractor.core.data.source.remote

import android.util.Log
import com.android.burdacontractor.core.data.source.remote.network.AksesBarangService
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.ApiService
import com.android.burdacontractor.core.data.source.remote.network.AuthService
import com.android.burdacontractor.core.data.source.remote.network.BarangService
import com.android.burdacontractor.core.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.core.data.source.remote.network.GudangService
import com.android.burdacontractor.core.data.source.remote.network.KendaraanService
import com.android.burdacontractor.core.data.source.remote.network.PeminjamanService
import com.android.burdacontractor.core.data.source.remote.network.PengembalianService
import com.android.burdacontractor.core.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.core.data.source.remote.network.PreOrderService
import com.android.burdacontractor.core.data.source.remote.network.ProyekService
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.network.UserService
import com.android.burdacontractor.core.data.source.remote.response2.TourismResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getAllSuratJalan(): Flow<ApiResponse<List<TourismResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getList()
                val dataArray = response.places
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.places))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}

