package com.android.burdacontractor.feature.perusahaan.presentation

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
import com.android.burdacontractor.core.presentation.adapter.PagingListPerusahaanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityPerusahaanBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerusahaanActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val perusahaanViewModel: PerusahaanViewModel by viewModels()
    private lateinit var filterDialog: FilterPerusahaanFragment
    private lateinit var adapter: PagingListPerusahaanAdapter
    private lateinit var binding: ActivityPerusahaanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.perusahaanBottomNavigation.menu.clear()
        when (storageViewModel.role) {
            UserRole.PURCHASING.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_purchasing, R.id.perusahaan_purchasing)
        }
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
                        setAdapter()
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
        with(binding) {
            filterDialog = FilterPerusahaanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    perusahaanViewModel.setSearch(searchView.text.toString())
                    setAdapter()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            if (storageViewModel.role == UserRole.PURCHASING.name) {
                btnAdd.setVisible()
                btnAdd.setOnClickListener {
//                    openActivity(AddPerusahaanActivity::class.java)
                }
            }
            binding.rvPerusahaan.layoutManager = GridLayoutManager(
                this@PerusahaanActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListPerusahaanAdapter { perusahaan ->
//                requireActivity().openActivityWithExtras(PerusahaanDetailActivity::class.java, false) {
//                    putString(INTENT_ID, perusahaan.id)
//                }
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyPerusahaan.setVisible()
                            } else {
                                binding.tvEmptyPerusahaan.setGone()
                            }
                            perusahaanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        if (adapter.itemCount == 0) {
                            perusahaanViewModel.setState(StateResponse.LOADING)
                        } else {
                            perusahaanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Error -> {
                        perusahaanViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvPerusahaan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {

                filterDialog.setOnClickListener(object :
                    FilterPerusahaanFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        perusahaanViewModel.getAllPerusahaan().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setBottomNavigationMenu(menu: Int, item: Int) {
        binding.perusahaanBottomNavigation.inflateMenu(menu)
        binding.perusahaanBottomNavigation.menu.findItem(item).isChecked = true
        binding.perusahaanBottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
                openActivity(BerandaActivity::class.java)
            }
            R.id.kendaraan_admin_gudang -> {
                openActivity(KendaraanActivity::class.java)
            }
            R.id.perusahaan_purchasing -> {
            }
            R.id.delivery_order_admin_gudang, R.id.delivery_order_logistic, R.id.delivery_order_purchasing -> {
                openActivity(DeliveryOrderActivity::class.java)
            }
        }
        return true
    }
}