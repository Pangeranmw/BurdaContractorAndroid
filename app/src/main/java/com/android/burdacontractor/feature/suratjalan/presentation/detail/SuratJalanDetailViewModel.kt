package com.android.burdacontractor.feature.suratjalan.presentation.detail
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.domain.usecase.DeleteSuratJalanChildUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.DeleteSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetSuratJalanByIdUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GiveTtdSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.MarkCompleteSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.SendSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuratJalanDetailViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getSuratJalanByIdUseCase: GetSuratJalanByIdUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val deleteSuratJalanUseCase: DeleteSuratJalanUseCase,
    private val deleteSuratJalanChildUseCase: DeleteSuratJalanChildUseCase,
    private val markCompleteSuratJalanUseCase: MarkCompleteSuratJalanUseCase,
    private val sendSuratJalanUseCase: SendSuratJalanUseCase,
    private val giveTtdSuratJalanUseCase: GiveTtdSuratJalanUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _id = MutableLiveData<String?>(null)
    val id: LiveData<String?> = _id

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _suratJalan = MutableLiveData<SuratJalanDetailItem>()
    val suratJalan: LiveData<SuratJalanDetailItem> = _suratJalan

    init {
        getUserByToken()
        viewModelScope.launch {
            _id.asFlow().collect {
                it?.let { id ->
                    getSuratJalanById(id)
                }
            }
        }
    }

    fun setId(id: String) {
        _id.value = id
    }

    private fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _user.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun sendSuratJalan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            sendSuratJalanUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun giveTtdSuratJalan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            giveTtdSuratJalanUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun markCompleteSuratJalan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            markCompleteSuratJalanUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun deleteSuratJalan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            deleteSuratJalanUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun deleteSuratJalanChild(id: String, tipe: String, listener: () -> Unit) {
        viewModelScope.launch {
            deleteSuratJalanChildUseCase.execute(id, tipe).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun getSuratJalanById(id: String) {
        viewModelScope.launch {
            getSuratJalanByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _suratJalan.value = res.data!!
                        _messageResponse.value = Event(res.message)
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }
}



