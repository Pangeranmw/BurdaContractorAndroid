package com.android.burdacontractor.feature.profile.presentation.users

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
import com.android.burdacontractor.core.presentation.adapter.PagingListUsersAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityUsersBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanActivity
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.profile.presentation.users.detail.UsersDetailActivity
import com.android.burdacontractor.feature.suratjalan.presentation.main.SuratJalanActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val storageViewModel: StorageViewModel by viewModels()
    private val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private lateinit var filterDialog: FilterUsersFragment
    private lateinit var adapter: PagingListUsersAdapter
    private lateinit var binding: ActivityUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        usersViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        with(usersViewModel) {
            state.observe(this@UsersActivity) {
                Log.d("addLoadStateListener", it.toString())
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            messageResponse.observe(this@UsersActivity) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            roleIndex.observe(this@UsersActivity) { roleIndex ->
                if (roleIndex != null) {
                    val listFilter = mutableListOf<FilterSelected>()
                    val filterAdapter = ListFilterSelectedAdapter {
                        if (it.index == 0) setRoleIndex(null)
                        listFilter.remove(it)
                        refreshData()
                    }
                    listFilter.add(FilterSelected(0, listRole.value!![roleIndex]))
                    binding.rvFilter.setVisible()
                    filterAdapter.submitList(listFilter)
                    binding.rvFilter.layoutManager = LinearLayoutManager(
                        this@UsersActivity,
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
            filterDialog = FilterUsersFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    usersViewModel.setSearch(searchView.text.toString())
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
//                    openActivity(AddUsersActivity::class.java, false)
                }
            }
            binding.rvUsers.layoutManager = GridLayoutManager(
                this@UsersActivity, 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListUsersAdapter { users ->
                openActivityWithExtras(UsersDetailActivity::class.java, false) {
                    putString(INTENT_ID, users.id)
                }
            }
            adapter.addLoadStateListener { loadState ->
                if ((loadState.refresh is LoadState.Loading) || (loadState.append is LoadState.Loading)) {
                    usersViewModel.setState(StateResponse.LOADING)
                } else if ((loadState.append is LoadState.NotLoading) && (loadState.refresh is LoadState.NotLoading)) {
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                        binding.tvEmptyUsers.setVisible()
                    } else {
                        binding.tvEmptyUsers.setGone()
                    }
                    usersViewModel.setState(StateResponse.SUCCESS)
                } else if ((loadState.refresh is LoadState.Error) || (loadState.append is LoadState.Error)) {
                    usersViewModel.setState(StateResponse.ERROR)
                }
            }
            rvUsers.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter { adapter.retry() }
            )
            refreshData()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterUsersFragment.OnClickListener {
                    override fun onClickListener() {
                        refreshData()
                    }
                })
                filterDialog.show(supportFragmentManager)
            }
        }
    }

    private fun refreshData() {
        usersViewModel.getAllUsers().observe(this) {
            adapter.submitData(lifecycle, it)
        }
        refreshBadgeValue()
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

            val navUser = navView.menu.findItem(R.id.nav_user)
            navUser.isChecked = true

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