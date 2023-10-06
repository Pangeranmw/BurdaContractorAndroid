package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivitySuratJalanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.PerusahaanActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivitySuratJalanBinding
    private lateinit var layout: View
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuratJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.mainLayout
        binding.suratJalanBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.surat_jalan_admin_gudang)
            UserRole.LOGISTIC.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.surat_jalan_logistic)
            UserRole.SITE_MANAGER.name, UserRole.SUPERVISOR.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_sv_pm, R.id.surat_jalan_sv_pm)
        }
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.suratJalanBottomNavigation.inflateMenu(menu)
        binding.suratJalanBottomNavigation.menu.findItem(item).isChecked = true
        binding.suratJalanBottomNavigation.setOnItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java)
            }
            R.id.surat_jalan_admin_gudang, R.id.surat_jalan_sv_pm, R.id.surat_jalan_logistic -> {

            }
            R.id.kendaraan_admin_gudang -> {
                openActivity(KendaraanActivity::class.java)
            }
            R.id.gudang_admin_gudang -> {
                openActivity(GudangActivity::class.java)
            }
            R.id.perusahaan_purchasing -> {
                openActivity(PerusahaanActivity::class.java)
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java)
            }
        }
        return true
    }
}