package com.android.burdacontractor.presentation.splashscreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.Manifest
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ActivityMainBinding
import com.android.burdacontractor.presentation.auth.LoginActivity
import com.android.burdacontractor.presentation.beranda.BerandaActivity
import com.android.burdacontractor.presentation.service.fcm.BurdaFirebaseMessagingService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        val nextIntent = if(mainViewModel.isUserLogin){
            Intent(this, BerandaActivity::class.java)
        }else{
            Intent(this, LoginActivity::class.java)
        }
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(nextIntent)
        finish()

        setContentView(binding.root)
    }
    companion object{
        private const val TAG = "MainActivity"
    }

}