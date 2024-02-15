package com.android.burdacontractor.feature.profile.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.profile.domain.model.User
import com.android.burdacontractor.feature.profile.domain.usecase.GetAllUsersUseCase
import com.android.burdacontractor.feature.profile.domain.usecase.UpdateRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllUsers: GetAllUsersUseCase,
    private val updateRoleUseCase: UpdateRoleUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _roleIndex = MutableLiveData<Int?>(null)
    val roleIndex: LiveData<Int?> = _roleIndex

    private val _listRole = MutableLiveData<List<String>>()
    val listRole: LiveData<List<String>> = _listRole

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    init {
        _listRole.value = listOf(
            UserRole.USER.name,
            UserRole.PURCHASING.name,
            UserRole.ADMIN_GUDANG.name,
            UserRole.ADMIN.name,
            UserRole.SITE_MANAGER.name,
            UserRole.SUPERVISOR.name,
            UserRole.PROJECT_MANAGER.name,
            UserRole.LOGISTIC.name,
        )
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun setRoleIndex(roleIndex: Int?) {
        _roleIndex.value = roleIndex
    }

    fun getAllUsers(): LiveData<PagingData<User>> {
        var filterRole: String? = null
        _roleIndex.value?.let { index ->
            _listRole.value?.let {
                filterRole = it[index]
            }
        }
        return getAllUsers.execute(
            size = 30,
            filter = filterRole,
            search = _search.value,
        ).cachedIn(viewModelScope)
    }

    fun updateRole(userId: String, role: String, listener: (() -> Unit)? = null) {
        viewModelScope.launch {
            updateRoleUseCase.execute(userId, role).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message.toString())
                        listener?.let { it() }
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }
}



