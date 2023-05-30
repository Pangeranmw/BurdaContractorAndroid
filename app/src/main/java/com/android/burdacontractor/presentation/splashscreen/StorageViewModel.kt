package com.android.burdacontractor.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(private val storageUseCase: StorageUseCase) : ViewModel() {
    val isUserLogin = storageUseCase.isUserLogin()
    val role = storageUseCase.getRole()
    val deviceToken = storageUseCase.getDeviceToken()
    val latitude = storageUseCase.getLatitude()
    val longitude = storageUseCase.getLongitude()
    val token = storageUseCase.getToken()
    val userId = storageUseCase.getUserId()
}

