package com.android.burdacontractor.feature.perusahaan.presentation.main

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import com.android.burdacontractor.core.presentation.BottomNavigationViewModel
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListPerusahaanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityPerusahaanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.FilterPerusahaanFragment
import com.android.burdacontractor.feature.perusahaan.presentation.create.AddPerusahaanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.detail.PerusahaanDetailActivity
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.suratjalan.presentation.main.SuratJalanActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerusahaanActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()
    private val perusahaanViewModel: PerusahaanViewModel by viewModels()
    private lateinit var filterDialog: FilterPerusahaanFragment
    private lateinit var adapter: PagingListPerusahaanAdapter
    private lateinit var binding: ActivityPerusahaanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        perusahaanViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        with(perusahaanViewModel) {
            state.observe(this@PerusahaanActivity) {
                Log.d("addLoadStateListener", it.toString())
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            setCoordinate(
                getDistanceMatrixCoordinate(
                    storageViewModel.latitude,
                    storageViewModel.longitude
                )
            )
            messageResponse.observe(this@PerusahaanActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            provinsiIndex.observe(this@PerusahaanActivity) { provinsiIndex ->
                if (provinsiIndex != null) {
                    val listFilter = mutableListOf<FilterSelected>()
                    val filterAdapter = ListFilterSelectedAdapter {
                        if (it.index == 0) setProvinsiIndex(null)
                        listFilter.remove(it)
                        refreshData()
                    }
                    listFilter.add(FilterSelected(0, listProvinsi.value!![provinsiIndex]))
                    binding.rvFilter.setVisible()
                    filterAdapter.submitList(listFilter)
                    binding.rvFilter.layoutManager = LinearLayoutManager(
                        this@PerusahaanActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.rvFilter.adapter = filterAdapter
                } else {
                    binding.rvFilter.setGone()
                }
            }
            initUi()
        }
    }

    private fun initUi() {
        initNavigation()
        initBadge()
        with(binding) {
            btnDrawer.setOnClickListener {
                mainLayout.openDrawer(GravityCompat.START)
            }
            filterDialog = FilterPerusahaanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    perusahaanViewModel.setSearch(searchView.text.toString())
                    refreshData()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                refreshData()
            }
            if (storageViewModel.role == UserRole.PURCHASING.name || storageViewModel.role == UserRole.ADMIN.name || storageViewModel.role == UserRole.ADMIN_GUDANG.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
                    openActivity(AddPerusahaanActivity::class.java, false)
                }
            }
            binding.rvPerusahaan.layoutManager = GridLayoutManager(
                this@PerusahaanActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListPerusahaanAdapter { perusahaan ->
                openActivityWithExtras(PerusahaanDetailActivity::class.java, false) {
                    putString(INTENT_ID, perusahaan.id)
                }
            }
            adapter.addLoadStateListener { loadState ->
                if ((loadState.refresh is LoadState.Loading) || (loadState.append is LoadState.Loading)) {
                    perusahaanViewModel.setState(StateResponse.LOADING)
                } else if ((loadState.append is LoadState.NotLoading) && (loadState.refresh is LoadState.NotLoading)) {
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                        binding.tvEmptyPerusahaan.setVisible()
                    } else {
                        binding.tvEmptyPerusahaan.setGone()
                    }
                    perusahaanViewModel.setState(StateResponse.SUCCESS)
                } else if ((loadState.refresh is LoadState.Error) || (loadState.append is LoadState.Error)) {
                    perusahaanViewModel.setState(StateResponse.ERROR)
                }
            }
            rvPerusahaan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter { adapter.retry() }
            )
            refreshData()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterPerusahaanFragment.OnClickListener {
                    override fun onClickListener() {
                        refreshData()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun refreshData() {
        perusahaanViewModel.getAllPerusahaan().observe(this) {
            adapter.submitData(lifecycle, it)
        }
        refreshBadgeValue()
        perusahaanViewModel.getPerusahaanProvinsi()
    }

    override fun onRestart() {
        super.onRestart()
        refreshData()
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
            navPerusahaan.isChecked = true

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
                openActivity(KendaraanActivity::class.java)
            }

            R.id.nav_gudang -> {
                openActivity(GudangActivity::class.java)
            }

            R.id.nav_perusahaan -> {
            }

            R.id.nav_delivery_order -> {
                openActivity(DeliveryOrderActivity::class.java)
            }

            R.id.nav_profile -> {
                openActivity(ProfileActivity::class.java, false)
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
            bottomNavigationViewModel.getCountActiveDeliveryOrder()
        }
        if (role == UserRole.LOGISTIC.name ||
            role == UserRole.ADMIN_GUDANG.name ||
            role == UserRole.ADMIN.name ||
            role == UserRole.SUPERVISOR.name ||
            role == UserRole.PROJECT_MANAGER.name ||
            role == UserRole.SITE_MANAGER.name
        ) {
            bottomNavigationViewModel.getCountActiveSuratJalan()
        }
    }

    private fun initBadge() {
        bottomNavigationViewModel.totalActiveSuratJalan.observe(this) {
            val badgeSj = binding.navView.menu.findItem(R.id.nav_surat_jalan).actionView as TextView
            badgeSj.text = it.toString()
            badgeSj.gravity = Gravity.CENTER_VERTICAL
            badgeSj.setTypeface(null, Typeface.BOLD)
            if (it > 0) badgeSj.setTextColor(ContextCompat.getColor(this, R.color.secondary_main))
            else badgeSj.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
        bottomNavigationViewModel.totalActiveDeliveryOrder.observe(this) {
            val badgeSj =
                binding.navView.menu.findItem(R.id.nav_delivery_order).actionView as TextView
            badgeSj.text = it.toString()
            badgeSj.gravity = Gravity.CENTER_VERTICAL
            badgeSj.setTypeface(null, Typeface.BOLD)
            if (it > 0) badgeSj.setTextColor(ContextCompat.getColor(this, R.color.secondary_main))
            else badgeSj.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }
}