package com.android.burdacontractor.feature.kendaraan.presentation.main

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.SidebarViewModel
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListKendaraanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityKendaraanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.FilterKendaraanFragment
import com.android.burdacontractor.feature.kendaraan.presentation.create.AddKendaraanActivity
import com.android.burdacontractor.feature.kendaraan.presentation.detail.KendaraanDetailActivity
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanActivity
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.profile.presentation.users.UsersActivity
import com.android.burdacontractor.feature.suratjalan.presentation.main.SuratJalanActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KendaraanActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val kendaraanViewModel: KendaraanViewModel by viewModels()
    private val sidebarViewModel: SidebarViewModel by viewModels()
    private lateinit var filterDialog: FilterKendaraanFragment
    private lateinit var adapter: PagingListKendaraanAdapter
    private lateinit var binding: ActivityKendaraanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKendaraanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        kendaraanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        with(kendaraanViewModel) {
            state.observe(this@KendaraanActivity) {
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            messageResponse.observe(this@KendaraanActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            statusIndex.observe(this@KendaraanActivity) { statusIndex ->
                jenisIndex.observe(this@KendaraanActivity) { jenisIndex ->
                    gudangIndex.observe(this@KendaraanActivity) { gudangIndex ->
                        val listFilter = mutableListOf<FilterSelected>()
                        val filterAdapter = ListFilterSelectedAdapter {
                            if (it.index == 0) setStatusIndex(null)
                            if (it.index == 1) setJenisIndex(null)
                            if (it.index == 2) setGudangIndex(null)
                            listFilter.remove(it)
                            refreshData()
                        }
                        if (statusIndex != null) {
                            listFilter.add(
                                FilterSelected(
                                    0,
                                    enumValueToNormal(listStatus.value!![statusIndex])
                                )
                            )
                        }
                        if (jenisIndex != null) {
                            listFilter.add(
                                FilterSelected(
                                    1,
                                    enumValueToNormal(listJenis.value!![jenisIndex])
                                )
                            )
                        }
                        if (gudangIndex != null) {
                            listFilter.add(FilterSelected(2, listGudang.value!![gudangIndex].nama))
                        }
                        if (listFilter.isNotEmpty()) binding.rvFilter.setVisible()
                        else binding.rvFilter.setGone()
                        filterAdapter.submitList(listFilter)
                        binding.rvFilter.layoutManager = LinearLayoutManager(
                            this@KendaraanActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        binding.rvFilter.adapter = filterAdapter
                    }
                }
            }
            initUi()
        }
    }

    private fun initNavigation() {
        binding.navView.setNavigationItemSelectedListener(this)
        binding.apply {
            val role = storageViewModel.role
            val navBeranda = navView.menu.findItem(R.id.nav_beranda)
            val navSJ = navView.menu.findItem(R.id.nav_surat_jalan)
            val navDO = navView.menu.findItem(R.id.nav_delivery_order)
            val navKendaraan = navView.menu.findItem(R.id.nav_kendaraan)
            val navGudang = navView.menu.findItem(R.id.nav_gudang)
            val navPerusahaan = navView.menu.findItem(R.id.nav_perusahaan)
            navKendaraan.isChecked = true

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
                openActivity(SuratJalanActivity::class.java)
            }

            R.id.nav_kendaraan -> {

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
        val role = storageViewModel.role
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
        initNavigation()
        initBadge()
        with(binding) {
            btnDrawer.setOnClickListener {
                mainLayout.openDrawer(GravityCompat.START)
            }
            filterDialog = FilterKendaraanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    kendaraanViewModel.setSearch(searchView.text.toString())
                    refreshData()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                refreshData()
            }
            if (storageViewModel.role == UserRole.ADMIN_GUDANG.name || storageViewModel.role == UserRole.ADMIN.name || storageViewModel.role == UserRole.PURCHASING.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
                    openActivity(AddKendaraanActivity::class.java, false)
                }
            }
            binding.rvKendaraan.layoutManager = GridLayoutManager(
                this@KendaraanActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListKendaraanAdapter { kendaraan ->
                openActivityWithExtras(KendaraanDetailActivity::class.java, false) {
                    putString(INTENT_ID, kendaraan.id)
                }
            }
            adapter.addLoadStateListener { loadState ->
                if ((loadState.refresh is LoadState.Loading) || (loadState.append is LoadState.Loading)) {
                    kendaraanViewModel.setState(StateResponse.LOADING)
                } else if ((loadState.append is LoadState.NotLoading) && (loadState.refresh is LoadState.NotLoading)) {
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                        binding.tvEmptyKendaraan.setVisible()
                    } else {
                        binding.tvEmptyKendaraan.setGone()
                    }
                    kendaraanViewModel.setState(StateResponse.SUCCESS)
                } else if ((loadState.refresh is LoadState.Error) || (loadState.append is LoadState.Error)) {
                    kendaraanViewModel.setState(StateResponse.ERROR)
                }
            }
            rvKendaraan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            refreshData()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterKendaraanFragment.OnClickListener {
                    override fun onClickListener() {
                        refreshData()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun refreshData() {
        kendaraanViewModel.getAllKendaraan().observe(this) {
            adapter.submitData(lifecycle, it)
        }
        refreshBadgeValue()
        kendaraanViewModel.getKendaraanGudang()
    }

    override fun onRestart() {
        super.onRestart()
        refreshData()
    }
}