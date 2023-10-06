package com.android.burdacontractor.feature.kendaraan.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityKendaraanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KendaraanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityKendaraanBinding
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKendaraanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.kendaraanBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.kendaraan_admin_gudang)
        }
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.kendaraanBottomNavigation.inflateMenu(menu)
        binding.kendaraanBottomNavigation.menu.findItem(item).isChecked = true
        binding.kendaraanBottomNavigation.setOnItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java)
            }
            R.id.surat_jalan_admin_gudang, R.id.surat_jalan_sv_pm, R.id.surat_jalan_logistic -> {
                openActivity(SuratJalanActivity::class.java)
            }
            R.id.kendaraan_admin_gudang -> {

            }
            R.id.gudang_admin_gudang -> {
                openActivity(GudangActivity::class.java)
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java)
            }
        }
        return true
    }
}