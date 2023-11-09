package com.android.burdacontractor.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetCountActiveDeliveryOrderUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetCountActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    private val getCountActiveSuratJalanUseCase: GetCountActiveSuratJalanUseCase,
    private val getCountActiveDeliveryOrderUseCase: GetCountActiveDeliveryOrderUseCase,
) : ViewModel() {

    private val _totalActiveSuratJalan = MutableLiveData<Int>()
    val totalActiveSuratJalan: LiveData<Int> = _totalActiveSuratJalan

    private val _totalActiveDeliveryOrder = MutableLiveData<Int>()
    val totalActiveDeliveryOrder: LiveData<Int> = _totalActiveDeliveryOrder

    init {
        getCountActiveSuratJalan()
        getCountActiveDeliveryOrder()
    }

    fun getCountActiveSuratJalan(){
        viewModelScope.launch {
            getCountActiveSuratJalanUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _totalActiveSuratJalan.value = it.data!!.totalActive
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }
    fun getCountActiveDeliveryOrder(){
        viewModelScope.launch {
            getCountActiveDeliveryOrderUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _totalActiveDeliveryOrder.value = it.data!!.totalActive
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }
}



