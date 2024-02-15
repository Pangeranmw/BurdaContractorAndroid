package com.android.burdacontractor.core.presentation

import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.domain.model.enums.Tema
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(private val storageUseCase: StorageUseCase) : ViewModel() {
    val role = storageUseCase.getPreferences(SessionManager.KEY_ROLE)
    val name = storageUseCase.getPreferences(SessionManager.KEY_NAME)
    val theme = storageUseCase.getPreferences(SessionManager.KEY_THEME)
    val ttd = storageUseCase.getPreferences(SessionManager.KEY_TTD)
    val deviceToken = storageUseCase.getPreferences(SessionManager.KEY_DEVICE_TOKEN)
    val latitude = storageUseCase.getPreferences(SessionManager.KEY_LATITUDE)
    val longitude = storageUseCase.getPreferences(SessionManager.KEY_LONGITUDE)
    val token = "Bearer ${storageUseCase.getPreferences(SessionManager.KEY_TOKEN)}"
    val userId = storageUseCase.getPreferences(SessionManager.KEY_USER_ID)
    val photo = storageUseCase.getPreferences(SessionManager.KEY_PHOTO)
    val isLogin = storageUseCase.isUserLogin()
    val getTracking = storageUseCase.getTracking()
    fun updateUser(user: UserByTokenItem) {
        storageUseCase.updateUser(user)
    }

    fun updateTtd(ttd: String) {
        storageUseCase.setPreferences(SessionManager.KEY_TTD, ttd)
    }

    fun setTracking(bool: Boolean) {
        storageUseCase.setTracking(bool)
    }

    fun setTheme(theme: Tema) {
        storageUseCase.setPreferences(SessionManager.KEY_THEME, theme.name)
    }
}