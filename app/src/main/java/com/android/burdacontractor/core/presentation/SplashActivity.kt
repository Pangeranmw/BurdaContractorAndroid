package com.android.burdacontractor.core.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
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
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(storageViewModel.isLogin){
            val clickAction = intent.getStringExtra("clickAction")
            if(clickAction!=null){
                val newIntent = Intent(clickAction)
                val intentKeyId = intent.getStringExtra("intentKeyId")
                val intentValueId = intent.getStringExtra("intentValueId")
                Log.d("INI EXTRA", "$intentKeyId $intentValueId $clickAction" )
                newIntent.putExtra(intentKeyId,intentValueId)
                startActivity(newIntent)
            }else{
                openActivity(BerandaActivity::class.java)
            }
        }else{
            openActivity(LoginActivity::class.java)
        }
    }

}