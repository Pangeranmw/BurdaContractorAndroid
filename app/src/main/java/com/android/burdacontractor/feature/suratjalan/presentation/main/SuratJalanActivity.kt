package com.android.burdacontractor.feature.suratjalan.presentation.main

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.SidebarViewModel
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivitySuratJalanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanActivity
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.android.burdacontractor.feature.profile.presentation.users.UsersActivity
import com.android.burdacontractor.feature.suratjalan.presentation.create.AddSuratJalanActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivitySuratJalanBinding
    private lateinit var layout: View
    private val sidebarViewModel: SidebarViewModel by viewModels()
    private val suratJalanViewModel: SuratJalanViewModel by viewModels()
    private lateinit var filterDialog: FilterSuratJalanFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuratJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.mainLayout
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        val selectedTab: String? = intent.getStringExtra(SELECTED_TAB)
        selectedTab?.let {
            suratJalanViewModel.setTipe(SuratJalanTipe.valueOf(it))
        }
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
            user.observe(this@SuratJalanActivity) {
                initUi()
            }
        }
    }

    private fun initNavigation() {
        binding.navView.setNavigationItemSelectedListener(this)
        binding.apply {
            val role = suratJalanViewModel.user.value!!.role
            val navBeranda = navView.menu.findItem(R.id.nav_beranda)
            val navSJ = navView.menu.findItem(R.id.nav_surat_jalan)
            val navDO = navView.menu.findItem(R.id.nav_delivery_order)
            val navKendaraan = navView.menu.findItem(R.id.nav_kendaraan)
            val navGudang = navView.menu.findItem(R.id.nav_gudang)
            val navPerusahaan = navView.menu.findItem(R.id.nav_perusahaan)
            navSJ.isChecked = true

            val navUser = navView.menu.findItem(R.id.nav_user)
            navUser.isVisible = role == UserRole.ADMIN.name

            navBeranda.isVisible = role != UserRole.USER.name

            val isVisibleDo = role == UserRole.LOGISTIC.name ||
                    role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.PURCHASING.name
            navDO.isVisible = isVisibleDo

            val isVisibleSj = role == UserRole.LOGISTIC.name ||
                    role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.SUPERVISOR.name ||
                    role == UserRole.PROJECT_MANAGER.name ||
                    role == UserRole.SITE_MANAGER.name ||
                    role == UserRole.PURCHASING.name
            navSJ.isVisible = isVisibleSj

            val isVisibleAAP = role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.PURCHASING.name
            navKendaraan.isVisible = isVisibleAAP
            navGudang.isVisible = isVisibleAAP
            navPerusahaan.isVisible = isVisibleAAP
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_beranda -> {
                openActivity(BerandaActivity::class.java)
            }

            R.id.nav_surat_jalan -> {
            }

            R.id.nav_kendaraan -> {
                openActivity(KendaraanActivity::class.java)
            }

            R.id.nav_gudang -> {
                openActivity(GudangActivity::class.java)
            }

            R.id.nav_perusahaan -> {
                openActivity(PerusahaanActivity::class.java)
            }

            R.id.nav_delivery_order -> {
                openActivity(DeliveryOrderActivity::class.java)
            }

            R.id.nav_profile -> {
                openActivity(ProfileActivity::class.java, false)
            }

            R.id.nav_user -> {
                openActivity(UsersActivity::class.java)
            }
        }
        return true
    }

    private fun refreshBadgeValue() {
        val role = suratJalanViewModel.user.value!!.role
        if (role == UserRole.LOGISTIC.name ||
            role == UserRole.ADMIN_GUDANG.name ||
            role == UserRole.ADMIN.name ||
            role == UserRole.PURCHASING.name
        ) {
            sidebarViewModel.getCountActiveDeliveryOrder()
        }
        if (role == UserRole.LOGISTIC.name ||
            role == UserRole.ADMIN_GUDANG.name ||
            role == UserRole.ADMIN.name ||
            role == UserRole.SUPERVISOR.name ||
            role == UserRole.PROJECT_MANAGER.name ||
            role == UserRole.SITE_MANAGER.name
        ) {
            sidebarViewModel.getCountActiveSuratJalan()
        }
    }

    private fun initBadge() {
        sidebarViewModel.totalActiveSuratJalan.observe(this) {
            val badgeSj = binding.navView.menu.findItem(R.id.nav_surat_jalan).actionView as TextView
            badgeSj.text = it.toString()
            badgeSj.gravity = Gravity.CENTER_VERTICAL
            badgeSj.setTypeface(null, Typeface.BOLD)
            if (it > 0) badgeSj.setTextColor(ContextCompat.getColor(this, R.color.secondary_main))
            else badgeSj.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
        sidebarViewModel.totalActiveDeliveryOrder.observe(this) {
            val badgeSj =
                binding.navView.menu.findItem(R.id.nav_delivery_order).actionView as TextView
            badgeSj.text = it.toString()
            badgeSj.gravity = Gravity.CENTER_VERTICAL
            badgeSj.setTypeface(null, Typeface.BOLD)
            if (it > 0) badgeSj.setTextColor(ContextCompat.getColor(this, R.color.secondary_main))
            else badgeSj.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }

    private fun initUi() {
        with(binding) {
            btnDrawer.setOnClickListener {
                mainLayout.openDrawer(GravityCompat.START)
            }
            initBadge()
            initNavigation()
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
            suratJalanViewModel.status.observe(this@SuratJalanActivity) {
                when (it) {
                    SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER -> {
                        tabs.selectTab(tabs.getTabAt(1))
                    }
                    SuratJalanStatus.DRIVER_DALAM_PERJALANAN -> {
                        tabs.selectTab(tabs.getTabAt(2))
                    }
                    SuratJalanStatus.SELESAI -> {
                        tabs.selectTab(tabs.getTabAt(3))
                    }
                    else -> {
                        tabs.selectTab(tabs.getTabAt(0))
                    }
                }
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
            if (suratJalanViewModel.user.value!!.role == UserRole.ADMIN_GUDANG.name || suratJalanViewModel.user.value!!.role == UserRole.PURCHASING.name || suratJalanViewModel.user.value!!.role == UserRole.ADMIN.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
                    if (suratJalanViewModel.user.value!!.ttd == null) {
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
                        openActivity(AddSuratJalanActivity::class.java, false)
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
        suratJalanViewModel.getUserByToken()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.semua,
            R.string.menunggu_driver,
            R.string.dalam_nperjalanan,
            R.string.selesai
        )
        const val SELECTED_TAB = "selected_tab"
    }
}