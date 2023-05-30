package com.android.burdacontractor.presentation.deliveryorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.ActivityDeliveryOrderBinding
import com.android.burdacontractor.presentation.beranda.BerandaActivity
import com.android.burdacontractor.presentation.gudang.GudangActivity
import com.android.burdacontractor.presentation.kendaraan.KendaraanActivity
import com.android.burdacontractor.presentation.perusahaan.PerusahaanActivity
import com.android.burdacontractor.presentation.splashscreen.StorageViewModel
import com.android.burdacontractor.presentation.suratjalan.SuratJalanActivity
import com.android.burdacontractor.presentation.suratjalan.SuratJalanPagerAdapter
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityDeliveryOrderBinding
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.deliveryOrderBottomNavigation.menu.clear()
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.delivery_order_admin_gudang)
            UserRole.LOGISTIC.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.delivery_order_logistic)
            UserRole.PURCHASING.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_purchasing, R.id.delivery_order_purchasing)
        }
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.deliveryOrderBottomNavigation.inflateMenu(menu)
        binding.deliveryOrderBottomNavigation.menu.findItem(item).isChecked = true
        binding.deliveryOrderBottomNavigation.setOnItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java, this)
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
            }
        }
        return true
    }
}