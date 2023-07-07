package com.android.burdacontractor.feature.beranda.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.service.location.LocationService
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityBerandaBinding
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.PerusahaanActivity
import com.android.burdacontractor.feature.suratjalan.presentation.BottomNavigationViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BerandaActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityBerandaBinding
    private lateinit var layout: View
    private val storageViewModel: StorageViewModel by viewModels()
    private val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        layout = binding.mainLayout
        binding.berandaBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.beranda_admin_gudang)
            UserRole.LOGISTIC.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.beranda_logistic)
            UserRole.PURCHASING.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_purchasing, R.id.beranda_purchasing)
            UserRole.PROJECT_MANAGER.name, UserRole.SUPERVISOR.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_sv_pm, R.id.beranda_sv_pm)
        }
        initBadge()
        startService()
        setContentView(binding.root)
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.berandaBottomNavigation.inflateMenu(menu)
        binding.berandaBottomNavigation.menu.findItem(item).isChecked = true
        binding.berandaBottomNavigation.setOnItemSelectedListener(this)
    }
    private fun initBadge(){
        bottomNavigationViewModel.totalActiveSuratJalan.observe(this){
            val badgeSjAdminGudang = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_admin_gudang)
            badgeSjAdminGudang.isVisible = true
            badgeSjAdminGudang.number = it

            val badgeSjSvPm = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_sv_pm)
            badgeSjSvPm.isVisible = true
            badgeSjSvPm.number = it

            val badgeSjLogistic = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_logistic)
            badgeSjLogistic.isVisible = true
            badgeSjLogistic.number = it
        }
        bottomNavigationViewModel.totalActiveDeliveryOrder.observe(this){
            val badgeDoAdminGudang = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_admin_gudang)
            badgeDoAdminGudang.isVisible = true
            badgeDoAdminGudang.number = it

            val badgeDoPurc = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_purchasing)
            badgeDoPurc.isVisible = true
            badgeDoPurc.number = it

            val badgeDoLogistic = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_logistic)
            badgeDoLogistic.isVisible = true
            badgeDoLogistic.number = it
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
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
            }
            R.id.surat_jalan_admin_gudang, R.id.surat_jalan_sv_pm, R.id.surat_jalan_logistic -> {
                openActivity(SuratJalanActivity::class.java, this)
            }
            R.id.kendaraan_admin_gudang -> {
                openActivity(KendaraanActivity::class.java, this)
            }
            R.id.gudang_admin_gudang -> {
                openActivity(GudangActivity::class.java, this)
            }
            R.id.perusahaan_purchasing -> {
                openActivity(PerusahaanActivity::class.java, this)
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java, this)
            }
        }
        return true
    }
}