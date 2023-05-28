package com.android.burdacontractor

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
open class BurdaApp: Application()