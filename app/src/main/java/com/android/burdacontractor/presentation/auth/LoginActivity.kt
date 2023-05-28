package com.android.burdacontractor.presentation.auth

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ActivityLoginBinding
import com.android.burdacontractor.presentation.LogisticViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val authViewModel: AuthViewModel by viewModels()
    val logisticViewModel: LogisticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}