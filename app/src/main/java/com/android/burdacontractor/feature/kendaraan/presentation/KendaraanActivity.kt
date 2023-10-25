package com.android.burdacontractor.feature.kendaraan.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListKendaraanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityKendaraanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KendaraanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val kendaraanViewModel: KendaraanViewModel by viewModels()
    private lateinit var filterDialog: FilterKendaraanFragment
    private lateinit var adapter: PagingListKendaraanAdapter
    private lateinit var binding: ActivityKendaraanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKendaraanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.kendaraanBottomNavigation.menu.clear()
        when (storageViewModel.role) {
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.kendaraan_admin_gudang)
        }
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
                            setAdapter()
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

    private fun initUi() {
        with(binding) {
            filterDialog = FilterKendaraanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    kendaraanViewModel.setSearch(searchView.text.toString())
                    setAdapter()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            if (storageViewModel.role == UserRole.ADMIN_GUDANG.name || storageViewModel.role == UserRole.ADMIN.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
//                    openActivity(AddKendaraanActivity::class.java)
                }
            }
            binding.rvKendaraan.layoutManager = GridLayoutManager(
                this@KendaraanActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListKendaraanAdapter { kendaraan ->
//                requireActivity().openActivityWithExtras(KendaraanDetailActivity::class.java, false) {
//                    putString(INTENT_ID, kendaraan.id)
//                }
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyKendaraan.setVisible()
                            } else {
                                binding.tvEmptyKendaraan.setGone()
                            }
                            kendaraanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        if (adapter.itemCount == 0) {
                            kendaraanViewModel.setState(StateResponse.LOADING)
                        } else {
                            kendaraanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Error -> {
                        kendaraanViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvKendaraan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterKendaraanFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        kendaraanViewModel.getAllKendaraan().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setBottomNavigationMenu(menu: Int, item: Int) {
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

            R.id.gudang_admin_gudang -> {
                openActivity(GudangActivity::class.java)
            }

            R.id.kendaraan_admin_gudang -> {
            }

            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java)
            }
        }
        return true
    }
}