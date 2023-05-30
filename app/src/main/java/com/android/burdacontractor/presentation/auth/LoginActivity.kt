package com.android.burdacontractor.presentation.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ActivityLoginBinding
import com.android.burdacontractor.presentation.LogisticViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    binding.root,
                    "The user denied the notifications ):",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Settings") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri: Uri =
                            Uri.fromParts("com.android.burdacontractor", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Snackbar.make(binding.root, "Notifikasi diizinkan", Snackbar.LENGTH_LONG)
            } else {
                Snackbar.make(binding.root, "Memerlukan perizinan notifikasi", Snackbar.LENGTH_LONG)
            }
        }
}