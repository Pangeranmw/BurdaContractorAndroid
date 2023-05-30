package com.android.burdacontractor.presentation.perusahaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityPerusahaanBinding
import com.android.burdacontractor.presentation.beranda.BerandaActivity
import com.android.burdacontractor.presentation.deliveryorder.DeliveryOrderActivity
import com.android.burdacontractor.presentation.gudang.GudangActivity
import com.android.burdacontractor.presentation.kendaraan.KendaraanActivity
import com.android.burdacontractor.presentation.splashscreen.StorageViewModel
import com.android.burdacontractor.presentation.suratjalan.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerusahaanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityPerusahaanBinding
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.perusahaanBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.PURCHASING.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_purchasing, R.id.perusahaan_purchasing)
        }
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.perusahaanBottomNavigation.inflateMenu(menu)
        binding.perusahaanBottomNavigation.menu.findItem(item).isChecked = true
        binding.perusahaanBottomNavigation.setOnItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java, this)
            }
            R.id.kendaraan_admin_gudang -> {
                openActivity(KendaraanActivity::class.java, this)
            }
            R.id.perusahaan_purchasing -> {
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java, this)
            }
        }
        return true
    }
}