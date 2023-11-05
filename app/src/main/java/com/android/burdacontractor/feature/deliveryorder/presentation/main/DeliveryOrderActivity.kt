package com.android.burdacontractor.feature.deliveryorder.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityDeliveryOrderBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.create.AddDeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanActivity
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityDeliveryOrderBinding
    private val storageViewModel: StorageViewModel by viewModels()
    private val deliveryOrderViewModel: DeliveryOrderViewModel by viewModels()
    private lateinit var filterDialog: FilterDeliveryOrderFragment
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
        val snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }
    }
    private fun refreshViewPager(viewPager: ViewPager2,position: Int){
        val fragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}") as DeliveryOrderFragment
        fragment.setAdapter(position+1, true)
    }
    private fun initObserver(){
        with(deliveryOrderViewModel){
            state.observe(this@DeliveryOrderActivity){
                binding.srLayout.isRefreshing = it==StateResponse.LOADING
            }
            messageResponse.observe(this@DeliveryOrderActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            createdByOrFor.observe(this@DeliveryOrderActivity){create->
                dateStart.observe(this@DeliveryOrderActivity){start->
                    dateEnd.observe(this@DeliveryOrderActivity){end->
                    }
                }
            }
        }
        initUi()
    }
    private fun initUi(){
        with(binding){
            filterDialog = FilterDeliveryOrderFragment.newInstance()
            val sectionsPagerAdapter = DeliveryOrderStatusPagerAdapter(this@DeliveryOrderActivity)
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    deliveryOrderViewModel.setSearch(searchView.text.toString())
                    searchView.hide()
                    refreshViewPager(viewPager, viewPager.currentItem)
                    false
                }
            srLayout.setOnRefreshListener {
                refreshViewPager(viewPager, viewPager.currentItem)
            }
            if (storageViewModel.role == UserRole.PURCHASING.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
                    if (storageViewModel.ttd.isBlank()) {
                        CustomDialog(
                            mainButtonText = "Tambah Tanda Tangan",
                            title = "Buat Tanda Tangan",
                            subtitle = "Anda belum memiliki tanda tangan, harap membuat tanda tangan terlebih dahulu",
                            blockMainButton = {
                                openActivity(SignatureActivity::class.java, false)
                            },
                            blockSecondaryButton = {}
                        ).show(supportFragmentManager, "MyCustomFragment")
                    } else {
                        openActivity(AddDeliveryOrderActivity::class.java, false)
                    }
                }
            }
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterDeliveryOrderFragment.OnClickListener {
                    override fun onClickListener() {
                        refreshViewPager(viewPager, viewPager.currentItem)
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
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
                openActivity(BerandaActivity::class.java)
            }
            R.id.surat_jalan_admin_gudang, R.id.surat_jalan_sv_pm, R.id.surat_jalan_logistic -> {
                openActivity(SuratJalanActivity::class.java)
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
            }
        }
        return true
    }
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.menunggu_driver,
            R.string.dalam_nperjalanan,
            R.string.selesai
        )
    }
}