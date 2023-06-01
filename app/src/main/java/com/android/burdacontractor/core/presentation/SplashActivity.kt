package com.android.burdacontractor.core.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityMainBinding
import com.android.burdacontractor.feature.auth.presentation.LoginActivity
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storageViewModel: StorageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(storageViewModel.isUserLogin){
            openActivity(BerandaActivity::class.java, this)
        }else{
            openActivity(LoginActivity::class.java, this)
        }
    }
    companion object{
        private const val TAG = "MainActivity"
    }

}