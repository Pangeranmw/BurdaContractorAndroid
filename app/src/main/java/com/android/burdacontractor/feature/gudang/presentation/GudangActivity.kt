package com.android.burdacontractor.feature.gudang.presentation

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
import com.android.burdacontractor.core.presentation.adapter.PagingListGudangAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityGudangBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GudangActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val gudangViewModel: GudangViewModel by viewModels()
    private lateinit var filterDialog: FilterGudangFragment
    private lateinit var adapter: PagingListGudangAdapter
    private lateinit var binding: ActivityGudangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGudangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.gudangBottomNavigation.menu.clear()
        when (storageViewModel.role) {
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.gudang_admin_gudang)
        }
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        gudangViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        with(gudangViewModel) {
            state.observe(this@GudangActivity) {
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            setCoordinate(
                getDistanceMatrixCoordinate(
                    storageViewModel.latitude,
                    storageViewModel.longitude
                )
            )
            messageResponse.observe(this@GudangActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            provinsiIndex.observe(this@GudangActivity) { provinsiIndex ->
                if (provinsiIndex != null) {
                    val listFilter = mutableListOf<FilterSelected>()
                    val filterAdapter = ListFilterSelectedAdapter {
                        if (it.index == 0) setProvinsiIndex(null)
                        listFilter.remove(it)
                        setAdapter()
                    }
                    listFilter.add(FilterSelected(0, listProvinsi.value!![provinsiIndex]))
                    binding.rvFilter.setVisible()
                    filterAdapter.submitList(listFilter)
                    binding.rvFilter.layoutManager = LinearLayoutManager(
                        this@GudangActivity,
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
        with(binding) {
            filterDialog = FilterGudangFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    gudangViewModel.setSearch(searchView.text.toString())
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
//                    openActivity(AddGudangActivity::class.java)
                }
            }
            binding.rvGudang.layoutManager = GridLayoutManager(
                this@GudangActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListGudangAdapter { gudang ->
//                requireActivity().openActivityWithExtras(GudangDetailActivity::class.java, false) {
//                    putString(INTENT_ID, gudang.id)
//                }
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyGudang.setVisible()
                            } else {
                                binding.tvEmptyGudang.setGone()
                            }
                            gudangViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        gudangViewModel.setState(StateResponse.LOADING)
                    }

                    is LoadState.Error -> {
                        gudangViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvGudang.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {

                filterDialog.setOnClickListener(object :
                    FilterGudangFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        gudangViewModel.getAllGudang().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setBottomNavigationMenu(menu: Int, item: Int) {
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