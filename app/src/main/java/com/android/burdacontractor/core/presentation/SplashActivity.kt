package com.android.burdacontractor.core.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        com.android.burdacontractor.core.utils.setTheme(storageViewModel.theme)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (storageViewModel.isLogin) {
            openActivity(BerandaActivity::class.java, true)
        } else {
            openActivity(LoginActivity::class.java, true)
        }
    }

}