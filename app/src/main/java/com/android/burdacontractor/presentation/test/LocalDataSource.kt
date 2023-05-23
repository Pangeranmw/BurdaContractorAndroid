package com.android.burdacontractor.presentation.test

import com.android.burdacontractor.core.data.source.local.entity.TourismEntity
import com.android.burdacontractor.core.data.source.local.room.TourismDao
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val tourismDao: TourismDao, private val sessionManager: SessionManager) {

    fun getAllTourism(): Flow<List<TourismEntity>> = tourismDao.getAllTourism()

    fun getFavoriteTourism(): Flow<List<TourismEntity>> = tourismDao.getFavoriteTourism()

    suspend fun insertTourism(tourismList: List<TourismEntity>) = tourismDao.insertTourism(tourismList)

    fun setFavoriteTourism(tourism: TourismEntity, newState: Boolean) {
        tourism.isFavorite = newState
        tourismDao.updateFavoriteTourism(tourism)
    }

    fun loginUser(userId: String, token: String) {
        sessionManager.createLoginSession()
        sessionManager.saveToPreference(SessionManager.KEY_USER_ID, userId)
        sessionManager.saveToPreference(SessionManager.KEY_TOKEN, token)
    }

    fun getUserId() = sessionManager.getFromPreference(SessionManager.KEY_USER_ID)

    fun getToken() = sessionManager.getFromPreference(SessionManager.KEY_TOKEN)

    fun isUserLogin() = sessionManager.isLogin

    fun logoutUser() = sessionManager.logout()
}