package com.android.burdacontractor.core.data.source.local.storage
import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    companion object {
        const val KEY_LOGIN = "isLogin"
        const val KEY_TOKEN = "token"
        const val KEY_USER_ID = "userId"
        const val KEY_ROLE = "role"
        const val KEY_TTD = "ttd"
        const val KEY_DEVICE_TOKEN = "deviceToken"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
        const val KEY_TRACKING = "isTracking"
    }

    private var pref: SharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    fun createLoginSession(isLogin: Boolean) {
        editor.putBoolean(KEY_LOGIN, isLogin).commit()
    }
    fun setTracking(isTracking: Boolean) {
        editor.putBoolean(KEY_TRACKING, isTracking).commit()
    }

    fun logout() {
//        editor.putBoolean(KEY_LOGIN, false)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_ROLE)
        editor.remove(KEY_LATITUDE)
        editor.remove(KEY_LONGITUDE)
        editor.remove(KEY_TTD)
        editor.remove(KEY_LOGIN)
        editor.remove(KEY_TRACKING)
//        editor.clear()
        editor.apply()
    }

    val isLogin: Boolean = pref.getBoolean(KEY_LOGIN, false)
    val isTracking: Boolean = pref.getBoolean(KEY_TRACKING, false)
    fun saveToPreference(key: String, value: String) = editor.putString(key, value).commit()

    fun getFromPreference(key: String) = pref.getString(key, "")

}