package com.android.burdacontractor.feature.suratjalan.presentation.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.BottomNavigationViewModel
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivitySuratJalanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivitySuratJalanBinding
    private lateinit var layout: View
    private val storageViewModel: StorageViewModel by viewModels()
    private val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()
    private val suratJalanViewModel: SuratJalanViewModel by viewModels()
    private lateinit var filterDialog: FilterSuratJalanFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuratJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.mainLayout
        binding.suratJalanBottomNavigation.menu.clear()
        when (storageViewModel.role) {
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(
                    R.menu.bottom_menu_admingudang,
                    R.id.surat_jalan_admin_gudang
                )

            UserRole.LOGISTIC.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.surat_jalan_logistic)

            UserRole.SITE_MANAGER.name, UserRole.SUPERVISOR.name, UserRole.PROJECT_MANAGER.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_sv_pm, R.id.surat_jalan_sv_pm)
        }
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        suratJalanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun refreshViewPager(viewPager: ViewPager2, position: Int) {
        val fragment =
            supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}") as SuratJalanFragment
        fragment.setAdapter(position + 1, true)
    }

    private fun initObserver() {
        with(suratJalanViewModel) {
            state.observe(this@SuratJalanActivity) {
                binding.srLayout.isRefreshing =
                    it == com.android.burdacontractor.core.domain.model.enums.StateResponse.LOADING
            }
            messageResponse.observe(this@SuratJalanActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            initBadge()
            filterDialog = FilterSuratJalanFragment.newInstance()
            val sectionsPagerAdapter = SuratJalanStatusPagerAdapter(this@SuratJalanActivity)
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    suratJalanViewModel.setSearch(searchView.text.toString())
                    searchView.hide()
                    refreshViewPager(viewPager, viewPager.currentItem)
                    false
                }
            srLayout.setOnRefreshListener {
                refreshViewPager(viewPager, viewPager.currentItem)
                refreshBadgeValue()
            }
            suratJalanViewModel.tipe.observe(this@SuratJalanActivity) {
                when (it) {
                    SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                        tabsTipe.selectTab(tabsTipe.getTabAt(0))
                    }

                    SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                        tabsTipe.selectTab(tabsTipe.getTabAt(1))
                    }

                    SuratJalanTipe.PENGEMBALIAN -> {
                        tabsTipe.selectTab(tabsTipe.getTabAt(2))
                    }
                }
            }
            tabsTipe.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tabsTipe.selectedTabPosition) {
                        0 -> {
                            suratJalanViewModel.setTipe(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
                        }

                        1 -> {
                            suratJalanViewModel.setTipe(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
                        }

                        2 -> {
                            suratJalanViewModel.setTipe(SuratJalanTipe.PENGEMBALIAN)
                        }
                    }
                    refreshViewPager(viewPager, viewPager.currentItem)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            if (storageViewModel.role == UserRole.ADMIN_GUDANG.name) {
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
//                        openActivity(AddSuratJalanActivity::class.java, false)
                    }
                }
            }
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterSuratJalanFragment.OnClickListener {
                    override fun onClickListener() {
                        refreshViewPager(viewPager, viewPager.currentItem)
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        refreshBadgeValue()
    }

    private fun refreshBadgeValue() {
        if (storageViewModel.role == UserRole.LOGISTIC.name ||
            storageViewModel.role == UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role == UserRole.ADMIN.name ||
            storageViewModel.role == UserRole.PURCHASING.name
        ) {
            bottomNavigationViewModel.getCountActiveSuratJalan()
        }
        if (storageViewModel.role == UserRole.LOGISTIC.name ||
            storageViewModel.role == UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role == UserRole.ADMIN.name
        ) {
            bottomNavigationViewModel.getCountActiveSuratJalan()
        }
    }

    private fun initBadge() {
        bottomNavigationViewModel.totalActiveSuratJalan.observe(this) {
            when (storageViewModel.role) {
                UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                    val badgeSjAdminGudang =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.surat_jalan_admin_gudang)
                    badgeSjAdminGudang.isVisible = true
                    badgeSjAdminGudang.number = it
                }

                UserRole.SUPERVISOR.name, UserRole.PROJECT_MANAGER.name, UserRole.SITE_MANAGER.name -> {
                    val badgeSjPmSvSm =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.surat_jalan_sv_pm)
                    badgeSjPmSvSm.isVisible = true
                    badgeSjPmSvSm.number = it
                }

                UserRole.LOGISTIC.name -> {
                    val badgeSjLogistic =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.surat_jalan_logistic)
                    badgeSjLogistic.isVisible = true
                    badgeSjLogistic.number = it
                }
            }
        }
        bottomNavigationViewModel.totalActiveDeliveryOrder.observe(this) {
            when (storageViewModel.role) {
                UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                    val badgeDoAdminGudang =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.delivery_order_admin_gudang)
                    badgeDoAdminGudang.isVisible = true
                    badgeDoAdminGudang.number = it
                }

                UserRole.PURCHASING.name -> {
                    val badgeDoPurc =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.delivery_order_purchasing)
                    badgeDoPurc.isVisible = true
                    badgeDoPurc.number = it
                }

                UserRole.LOGISTIC.name -> {
                    val badgeDoLogistic =
                        binding.suratJalanBottomNavigation.getOrCreateBadge(R.id.delivery_order_logistic)
                    badgeDoLogistic.isVisible = true
                    badgeDoLogistic.number = it
                }
            }
        }
    }

    private fun setBottomNavigationMenu(menu: Int, item: Int) {
        binding.suratJalanBottomNavigation.inflateMenu(menu)
        binding.suratJalanBottomNavigation.menu.findItem(item).isChecked = true
        binding.suratJalanBottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java)
            }
            R.id.kendaraan_admin_gudang -> {
                openActivity(KendaraanActivity::class.java)
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.menunggu_driver,
            R.string.dalam_nperjalanan,
            R.string.selesai
        )
    }
}