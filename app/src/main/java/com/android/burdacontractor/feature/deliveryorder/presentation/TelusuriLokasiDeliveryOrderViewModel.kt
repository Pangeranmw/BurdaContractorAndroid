package com.android.burdacontractor.feature.deliveryorder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetDeliveryOrderByIdUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetCountActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelusuriLokasiDeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getDistanceMatrixUseCase: GetDistanceMatrixUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _distanceMatrix = MutableLiveData<DistanceMatrixResponse>()
    val distanceMatrix: LiveData<DistanceMatrixResponse> = _distanceMatrix

    fun getDistanceMatrix(coordinates:String){
        viewModelScope.launch {
            getDistanceMatrixUseCase.execute(coordinates).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _distanceMatrix.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
}



