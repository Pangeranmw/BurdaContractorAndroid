package com.android.burdacontractor.feature.beranda.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticUseCase
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.DataAllSuratJalanWithCount
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanDalamPerjalananByUserUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetSomeActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BerandaViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val getSomeActiveSuratJalanUseCase: GetSomeActiveSuratJalanUseCase,
    private val getKendaraanByLogisticUseCase: GetKendaraanByLogisticUseCase,
    private val getAllSuratJalanDalamPerjalananByUserUseCase: GetAllSuratJalanDalamPerjalananByUserUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _sjPengirimanGp = MutableLiveData<DataAllSuratJalanWithCount>()
    val sjPengirimanGp: LiveData<DataAllSuratJalanWithCount> = _sjPengirimanGp

    private val _sjPengirimanPp = MutableLiveData<DataAllSuratJalanWithCount>()
    val sjPengirimanPp: LiveData<DataAllSuratJalanWithCount> = _sjPengirimanPp

    private val _sjPengembalian = MutableLiveData<DataAllSuratJalanWithCount>()
    val sjPengembalian: LiveData<DataAllSuratJalanWithCount> = _sjPengembalian

    private val _sjDalamPerjalanan = MutableLiveData<List<AllSuratJalan>>()
    val sjDalamPerjalanan: LiveData<List<AllSuratJalan>> = _sjDalamPerjalanan

    private val _kendaraanByLogistic = MutableLiveData<KendaraanByLogistic?>()
    val kendaraanByLogistic: LiveData<KendaraanByLogistic?> = _kendaraanByLogistic

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    init {
        getUserByToken()
        getSomeActiveSuratJalan(SuratJalanTipe.PENGEMBALIAN)
        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
        getAllSuratJalanDalamPerjalananByUser()
        getKendaraanByLogistic()
    }

    fun getUserByToken(){
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _user.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getSomeActiveSuratJalan(tipe: SuratJalanTipe){
        viewModelScope.launch {
            getSomeActiveSuratJalanUseCase.execute(tipe).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        when (tipe) {
                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                                _sjPengirimanGp.value = it.data!!
                            }
                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                                _sjPengirimanPp.value = it.data!!
                            }
                            SuratJalanTipe.PENGEMBALIAN -> {
                                _sjPengembalian.value = it.data!!
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getAllSuratJalanDalamPerjalananByUser(){
        viewModelScope.launch {
            getAllSuratJalanDalamPerjalananByUserUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _sjDalamPerjalanan.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getKendaraanByLogistic(){
        viewModelScope.launch {
            getKendaraanByLogisticUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _kendaraanByLogistic.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _kendaraanByLogistic.value = it.data
                    }
                }
            }
        }
    }

}



