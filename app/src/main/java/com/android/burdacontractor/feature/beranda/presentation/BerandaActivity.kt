package com.android.burdacontractor.feature.beranda.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.BottomNavigationViewModel
import com.android.burdacontractor.core.presentation.LogisticFirebaseViewModel
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListStatistikMenungguSuratJalanAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.service.location.LocationService
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityBerandaBinding
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.main.DeliveryOrderActivity
import com.android.burdacontractor.feature.gudang.presentation.main.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.detail.KendaraanDetailActivity
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.android.burdacontractor.feature.profile.presentation.users.UsersActivity
import com.android.burdacontractor.feature.suratjalan.presentation.detail.SuratJalanDetailActivity
import com.android.burdacontractor.feature.suratjalan.presentation.main.SuratJalanActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BerandaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityBerandaBinding
    private val storageViewModel: StorageViewModel by viewModels()
    private val logisticFirebaseViewModel: LogisticFirebaseViewModel by viewModels()
    private val berandaViewModel: BerandaViewModel by viewModels()
    private val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()
    private lateinit var adapterStatSJ: ListStatistikMenungguSuratJalanAdapter
    private lateinit var adapterSjPengembalian: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanGp: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanPp: ListSuratJalanAdapter
    private lateinit var adapterSjDalamPerjalanan: ListSuratJalanAdapter
    private lateinit var adapterDeliveryOrder: ListDeliveryOrderAdapter
    private lateinit var adapterDoDalamPerjalanan: ListDeliveryOrderAdapter
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBadgeAndHideLayout()
        initAppBar(
            UserByTokenItem(
                role = storageViewModel.role,
                nama = storageViewModel.name,
                foto = storageViewModel.photo,
                noHp = null,
                updatedAt = null,
                ttd = null,
                deviceToken = null,
                createdAt = null,
                id = "",
                email = null
            )
        )
        berandaViewModel.user.observe(this) { user ->
            storageViewModel.updateUser(user)
            initAppBar(user)
            binding.layoutTtd.isVisible = user.ttd == null || storageViewModel.ttd.isBlank()
        }
        berandaViewModel.role.observe(this) {
            it?.let {
                initBadgeAndHideLayout()
                refreshBadgeValue(it)
                initLayout(it)
            }
        }
        snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        berandaViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
        checkUserTracking()
    }

    private fun refreshBadgeValue(role: String) {
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
    private fun initBadgeAndHideLayout(){
        binding.layoutDeliveryOrder.setGone()
        binding.layoutKendaraan.setGone()
        binding.layoutSjPengembalian.setGone()
        binding.layoutSjPengirimanGp.setGone()
        binding.layoutSjPengirimanPp.setGone()
        binding.layoutSjDalamPerjalanan.setGone()
        binding.layoutDoDalamPerjalanan.setGone()
        binding.layoutMenungguSuratJalan.setGone()
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
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    checkUserTracking()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    checkUserTracking()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    override fun onRestart() {
        super.onRestart()
        berandaViewModel.getAllData()
        berandaViewModel.role.value?.let {
            refreshBadgeValue(it)
        }
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkUserTracking() {
        if (storageViewModel.getTracking && storageViewModel.role != UserRole.LOGISTIC.name) startService()
        if (storageViewModel.role == UserRole.LOGISTIC.name) {
            logisticFirebaseViewModel.getIsTrackingRealtime(storageViewModel.userId)
            logisticFirebaseViewModel.isTracking.observe(this) {
                if (it) startService()
            }
        }
    }
    private fun startService() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_beranda -> {}
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
                openActivity(UsersActivity::class.java)
            }
        }
        return true
    }
    private fun initObserver() {
        berandaViewModel.state.observe(this){
            binding.srLayout.isRefreshing = it==StateResponse.LOADING
        }
        berandaViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        berandaViewModel.sjDalamPerjalanan.observe(this){sjDalamPerjalanan->
            adapterSjDalamPerjalanan = initAdapterSj()
            initRvSj(binding.rvSjDalamPerjalanan,adapterSjDalamPerjalanan)
            adapterSjDalamPerjalanan.submitList(sjDalamPerjalanan)
            initCountTextAndButton(sjDalamPerjalanan.size, binding.tvCountSjDalamPerjalanan)
        }
        berandaViewModel.doDalamPerjalanan.observe(this){doDalamPerjalanan->
            adapterDoDalamPerjalanan = initAdapterDeliveryOrder()
            initRvDo(binding.rvDoDalamPerjalanan,adapterDoDalamPerjalanan)
            adapterDoDalamPerjalanan.submitList(doDalamPerjalanan)
            initCountTextAndButton(doDalamPerjalanan.size, binding.tvCountDoDalamPerjalanan)
        }
        berandaViewModel.sjPengembalian.observe(this){sjPengembalian->
            adapterSjPengembalian = initAdapterSj()
            initRvSj(binding.rvSjPengembalian,adapterSjPengembalian)
            adapterSjPengembalian.submitList(sjPengembalian.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengembalian.count!!,binding.tvCountSjPengembalian,binding.btnSeeAllSjPengembalian)
        }
        berandaViewModel.sjPengirimanGp.observe(this){sjPengirimanGp->
            adapterSjPengirimanGp = initAdapterSj()
            initRvSj(binding.rvSjPengirimanGp,adapterSjPengirimanGp)
            adapterSjPengirimanGp.submitList(sjPengirimanGp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengirimanGp.count!!,binding.tvCountSjPengirimanGp,binding.btnSeeAllSjPengirimanGp)
        }
        berandaViewModel.sjPengirimanPp.observe(this){sjPengirimanPp->
            adapterSjPengirimanPp = initAdapterSj()
            initRvSj(binding.rvSjPengirimanPp,adapterSjPengirimanPp)
            adapterSjPengirimanPp.submitList(sjPengirimanPp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengirimanPp.count!!,binding.tvCountSjPengirimanPp,binding.btnSeeAllSjPengirimanPp)
        }
        berandaViewModel.deliveryOrder.observe(this){deliveryOrder->
            adapterDeliveryOrder = initAdapterDeliveryOrder()
            initRvDo(binding.rvDeliveryOrder,adapterDeliveryOrder)
            val createdByOrFor = deliveryOrder.deliveryOrder!!.filterNot{it.status == DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name && it.idPurchasing != storageViewModel.userId}
            adapterDeliveryOrder.submitList(createdByOrFor)
            initCountTextAndButton(
                deliveryOrder.count!!,
                binding.tvCountDeliveryOrder,
                binding.btnSeeAllDeliveryOrder
            )
        }
        initUi()
    }

    private fun initCountTextAndButton(count: Int, tvCount: TextView, btn: Button? = null) {
        tvCount.text = count.toString()
        if (count > 0) {
            tvCount.maxWidth = 240
            tvCount.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            btn?.setVisible()
        } else {
            tvCount.maxWidth = 500
            tvCount.setBackgroundResource(R.drawable.semi_rounded_red)
            btn?.setGone()
        }
    }

    private fun initLayout(role: String) {
        binding.apply {
            navView.setNavigationItemSelectedListener(this@BerandaActivity)
            tvUserCantUseApp.isVisible = role == UserRole.USER.name
            val navBeranda = navView.menu.findItem(R.id.nav_beranda)
            val navSJ = navView.menu.findItem(R.id.nav_surat_jalan)
            val navDO = navView.menu.findItem(R.id.nav_delivery_order)
            val navKendaraan = navView.menu.findItem(R.id.nav_kendaraan)
            val navGudang = navView.menu.findItem(R.id.nav_gudang)
            val navPerusahaan = navView.menu.findItem(R.id.nav_perusahaan)

            navBeranda.isChecked = true

            navBeranda.isVisible = role != UserRole.USER.name

            val navUser = navView.menu.findItem(R.id.nav_user)
            navUser.isVisible = role == UserRole.ADMIN.name

            val isVisibleDo = role == UserRole.LOGISTIC.name ||
                    role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.PURCHASING.name
            navDO.isVisible = isVisibleDo
            layoutDeliveryOrder.isVisible = isVisibleDo
            layoutDoDalamPerjalanan.isVisible = isVisibleDo

            val isVisibleSj = role == UserRole.LOGISTIC.name ||
                    role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.SUPERVISOR.name ||
                    role == UserRole.PROJECT_MANAGER.name ||
                    role == UserRole.SITE_MANAGER.name ||
                    role == UserRole.PURCHASING.name
            navSJ.isVisible = isVisibleSj
            layoutSjPengembalian.isVisible = isVisibleSj
            layoutSjPengirimanGp.isVisible = isVisibleSj
            layoutSjPengirimanPp.isVisible = isVisibleSj
            layoutSjDalamPerjalanan.isVisible = isVisibleSj

            val isVisibleAAP = role == UserRole.ADMIN_GUDANG.name ||
                    role == UserRole.ADMIN.name ||
                    role == UserRole.PURCHASING.name
            navKendaraan.isVisible = isVisibleAAP
            navGudang.isVisible = isVisibleAAP
            navPerusahaan.isVisible = isVisibleAAP
            layoutMenungguSuratJalan.isVisible = isVisibleAAP
            if (isVisibleAAP) berandaViewModel.statistikMenungguSuratJalan.observe(this@BerandaActivity) { stat ->
                adapterStatSJ = ListStatistikMenungguSuratJalanAdapter()
                adapterStatSJ.submitList(stat)
                rvMenungguSuratJalan.layoutManager =
                    GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
                rvMenungguSuratJalan.adapter = adapterStatSJ
            }

            val isLogistic = role == UserRole.LOGISTIC.name
            layoutKendaraan.isVisible = isLogistic
            if (isLogistic) berandaViewModel.kendaraanByLogistic.observe(this@BerandaActivity) {
                if (it == null) {
                    cvKendaraan.setGone()
                    tvEmptyKendaraan.setVisible()
                } else {
                    tvEmptyKendaraan.setGone()
                    cvKendaraan.setVisible()
                    tvGudangKendaraan.text = it.namaGudang
                    tvAlamatKendaraan.text = it.alamatGudang
                    tvMerkKendaraan.text = it.merk
                    tvPlatKendaraan.text = it.platNomor
                    ivKendaraan.setImageFromUrl(it.gambar, this@BerandaActivity)
                    when (it.jenis) {
                        JenisKendaraan.MINIBUS.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_minibus)
                        JenisKendaraan.MOBIL.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_car)
                        JenisKendaraan.MOTOR.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_motorcycle)
                        JenisKendaraan.PICKUP.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_pickup)
                        JenisKendaraan.TRONTON.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_tronton)
                        JenisKendaraan.TRUCK.name -> ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_box)
                    }
                    cvKendaraan.setOnClickListener { view ->
                        openActivityWithExtras(KendaraanDetailActivity::class.java, false) {
                            putString(INTENT_ID, it.id)
                        }
                    }
                }
            }
        }
    }

    private fun initAppBar(user: UserByTokenItem) {
        binding.tvNamaUser.text = user.nama
        binding.tvRoleUser.text = enumValueToNormal(user.role)
        if (user.foto != null && user.foto.toString().isNotBlank()) {
            binding.ivUser.setImageFromUrl(user.foto, applicationContext)
        }
        binding.ivUser.setOnClickListener {
            openActivity(ProfileActivity::class.java, false)
        }
    }

    private fun initAdapterDeliveryOrder(): ListDeliveryOrderAdapter {
        return ListDeliveryOrderAdapter(storageViewModel.role, storageViewModel.userId) {
            openActivityWithExtras(DeliveryOrderDetailActivity::class.java, false) {
                putString(INTENT_ID, it.id)
            }
        }
    }
    private fun initAdapterSj(): ListSuratJalanAdapter{
        return ListSuratJalanAdapter(storageViewModel.role, storageViewModel.userId) {
            openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                putString(INTENT_ID, it.id)
            }
        }
    }
    private fun initRvSj(rv: RecyclerView, adapter: ListSuratJalanAdapter){
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
    private fun initRvDo(rv: RecyclerView, adapter: ListDeliveryOrderAdapter){
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
    private fun initUi(){
        with(binding) {
            btnDrawer.setOnClickListener {
                mainLayout.openDrawer(GravityCompat.START)
            }
            btnTtd.setOnClickListener {
                openActivity(SignatureActivity::class.java, false)
            }
            srLayout.setOnRefreshListener {
                berandaViewModel.getAllData()
                berandaViewModel.role.value?.let {
                    refreshBadgeValue(it)
                }
            }
            btnSeeAllDeliveryOrder.setOnClickListener {
                openActivity(DeliveryOrderActivity::class.java)
            }
            btnSeeAllSjPengembalian.setOnClickListener {
                openActivityWithExtras(SuratJalanActivity::class.java) {
                    putString(SuratJalanActivity.SELECTED_TAB, SuratJalanTipe.PENGEMBALIAN.name)
                }
            }
            btnSeeAllSjPengirimanGp.setOnClickListener {
                openActivityWithExtras(SuratJalanActivity::class.java) {
                    putString(
                        SuratJalanActivity.SELECTED_TAB,
                        SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name
                    )
                }
            }
            btnSeeAllSjPengirimanPp.setOnClickListener {
                openActivityWithExtras(SuratJalanActivity::class.java) {
                    putString(
                        SuratJalanActivity.SELECTED_TAB,
                        SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name
                    )
                }
            }
        }
    }
}