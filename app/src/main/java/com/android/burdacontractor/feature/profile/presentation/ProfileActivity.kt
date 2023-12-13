package com.android.burdacontractor.feature.profile.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.service.location.LocationService
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityProfileBinding
import com.android.burdacontractor.feature.auth.presentation.LoginActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private var snackbar: Snackbar? = null
    private lateinit var user: UserByTokenItem
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
            binding.progressBar.isVisible = it==StateResponse.LOADING
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
        profileViewModel.user.observe(this) { user ->
            this.user = user
            initProfile()
            initUi()
        }
    }

    override fun onRestart() {
        super.onRestart()
        profileViewModel.getUserByToken()
    }

    private fun initProfile() {
        binding.tvNamaUser.text = user.nama
        binding.tvRolUser.text = enumValueToNormal(user.role)
        binding.tvNoHpUser.text = user.noHp
        binding.tvEmailUser.text = user.email
        if (user.foto != null) {
            Glide.with(this)
                .load(getPhotoUrl(user.foto!!))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivUser)
        }
    }

    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    private fun initUi() {
        onBackPressedCallback()
        binding.btnLogout.setOnClickListener {
            stopService()
            profileViewModel.logout {
                openActivity(
                    LoginActivity::class.java,
                    isFinished = true,
                    clearAllTask = true
                )
            }
        }
        if(user.role == UserRole.LOGISTIC.name){
            profileViewModel.getIsTrackingRealtime(user.id)
            profileViewModel.isTracking.observe(this){
                binding.switchTracking.isChecked = it
            }
        }else {
//            binding.switchTracking.isChecked = profileViewModel.getIsTrackingStorage
            binding.switchTracking.isChecked = storageViewModel.getTracking
        }
        binding.switchTracking.setOnCheckedChangeListener { _, b ->
            if(user.role == UserRole.LOGISTIC.name){
                profileViewModel.setIsTrackingRealtime(user.id, b)
            }else {
//                profileViewModel.setIsTrackingStorage(b)
                storageViewModel.setTracking(b)
            }
            if(!b) {
                stopService()
                binding.tvSwitchTracking.text = getString(R.string.tracking_lokasi_tidak_aktif)
            } else {
                startService()
                binding.tvSwitchTracking.text = getString(R.string.tracking_lokasi_aktif)
            }
        }
        binding.rlUbahTtd.setOnClickListener {
            openActivity(SignatureActivity::class.java, false)
        }
        binding.rlUbahProfil.setOnClickListener {
            openActivity(UpdateProfileActivity::class.java, false)
        }
        binding.rlUbahPassword.setOnClickListener {
            openActivity(UpdatePasswordActivity::class.java, false)
        }
        binding.rlUbahFoto.setOnClickListener {
            openActivity(UploadPhotoActivity::class.java, false)
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
                    startService()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    startService()
                }
                else -> {}
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