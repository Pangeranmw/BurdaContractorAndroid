package com.android.burdacontractor.feature.profile.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.service.location.LocationService
import com.android.burdacontractor.core.utils.*
import com.android.burdacontractor.databinding.ActivityProfileBinding
import com.android.burdacontractor.feature.auth.presentation.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        snackbar= Snackbar.make(findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
        profileViewModel.liveNetworkChecker.observe(this){
            this.checkConnection(snackbar,it){ initObserver() }
        }
        setContentView(binding.root)
    }
    private fun initObserver() {
        profileViewModel.state.observe(this){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                }
                else -> {}
            }
        }
        profileViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        profileViewModel.user.observe(this){user->
            initProfile(user)
        }
        initUi()
    }
    private fun initProfile(user: User){
        binding.tvNamaUser.text = user.nama
        binding.tvRolUser.text = enumValueToNormal(user.role)
        binding.tvNoHpUser.text = user.noHp
        if(user.foto !=null){
            Glide.with(this)
                .load(getPhotoUrl(user.foto))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivUser)
        }
    }
    private fun initUi(){
        binding.btnLogout.setOnClickListener {
            stopService()
            applicationContext.openActivity(LoginActivity::class.java, this)
        }
        binding.switchTracking.setOnCheckedChangeListener { _, b ->
            if(!b) {
                stopService()
                binding.tvSwitchTracking.text = getString(R.string.tracking_lokasi_tidak_aktif)
            }
            else {
                startService()
                binding.tvSwitchTracking.text = getString(R.string.tracking_lokasi_aktif)
            }
        }
    }
    private fun stopService(){
        Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            stopService(this)
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    startService()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    startService()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun startService() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}