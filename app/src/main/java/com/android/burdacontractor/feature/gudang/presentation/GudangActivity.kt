package com.android.burdacontractor.feature.gudang.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityGudangBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderActivity
import com.android.burdacontractor.feature.kendaraan.presentation.KendaraanActivity
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GudangActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityGudangBinding
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGudangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.gudangBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.gudang_admin_gudang)
        }
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.gudangBottomNavigation.inflateMenu(menu)
        binding.gudangBottomNavigation.menu.findItem(item).isChecked = true
        binding.gudangBottomNavigation.setOnItemSelectedListener(this)
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
                openActivity(KendaraanActivity::class.java)
            }
            R.id.gudang_admin_gudang -> {
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java)
            }
        }
        return true
    }
}