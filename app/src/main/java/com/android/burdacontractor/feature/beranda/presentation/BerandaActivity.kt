package com.android.burdacontractor.feature.beranda.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.UserRole
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
import com.android.burdacontractor.feature.gudang.presentation.GudangActivity
import com.android.burdacontractor.feature.kendaraan.presentation.KendaraanActivity
import com.android.burdacontractor.feature.perusahaan.presentation.PerusahaanActivity
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.suratjalan.presentation.BottomNavigationViewModel
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanDetailActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BerandaActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
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

        binding.berandaBottomNavigation.menu.clear()
        berandaViewModel.user.observe(this){user->
            storageViewModel.updateUser(user)
            berandaViewModel.role.observe(this){
                if(it==null) berandaViewModel.setRole(user.role)
            }
        }
        when(storageViewModel.role){
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_admingudang, R.id.beranda_admin_gudang)
            UserRole.LOGISTIC.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_logistic, R.id.beranda_logistic)
            UserRole.PURCHASING.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_purchasing, R.id.beranda_purchasing)
            UserRole.SITE_MANAGER.name, UserRole.SUPERVISOR.name, UserRole.PROJECT_MANAGER.name ->
                setBottomNavigationMenu(R.menu.bottom_menu_sv_pm, R.id.beranda_sv_pm)
        }
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        berandaViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }
        initBadgeAndHideLayout()
        checkUserTracking()
    }
    private fun setBottomNavigationMenu(menu: Int, item: Int){
        binding.berandaBottomNavigation.inflateMenu(menu)
        binding.berandaBottomNavigation.menu.findItem(item).isChecked = true
        binding.berandaBottomNavigation.setOnItemSelectedListener(this)
    }
    private fun refreshBadgeValue(){
        if(storageViewModel.role==UserRole.LOGISTIC.name ||
            storageViewModel.role==UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role==UserRole.ADMIN.name ||
            storageViewModel.role==UserRole.PURCHASING.name
        ) {
            bottomNavigationViewModel.getCountActiveDeliveryOrder()
        }
        if(storageViewModel.role==UserRole.LOGISTIC.name ||
            storageViewModel.role==UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role==UserRole.ADMIN.name ||
            storageViewModel.role==UserRole.SUPERVISOR.name ||
            storageViewModel.role==UserRole.PROJECT_MANAGER.name ||
            storageViewModel.role==UserRole.SITE_MANAGER.name
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
        bottomNavigationViewModel.totalActiveSuratJalan.observe(this){
            val badgeSjAdminGudang = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_admin_gudang)
            badgeSjAdminGudang.isVisible = true
            badgeSjAdminGudang.number = it

            val badgeSjSvPm = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_sv_pm)
            badgeSjSvPm.isVisible = true
            badgeSjSvPm.number = it

            val badgeSjLogistic = binding.berandaBottomNavigation.getOrCreateBadge(R.id.surat_jalan_logistic)
            badgeSjLogistic.isVisible = true
            badgeSjLogistic.number = it
        }
        bottomNavigationViewModel.totalActiveDeliveryOrder.observe(this){
            val badgeDoAdminGudang = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_admin_gudang)
            badgeDoAdminGudang.isVisible = true
            badgeDoAdminGudang.number = it

            val badgeDoPurc = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_purchasing)
            badgeDoPurc.isVisible = true
            badgeDoPurc.number = it

            val badgeDoLogistic = binding.berandaBottomNavigation.getOrCreateBadge(R.id.delivery_order_logistic)
            badgeDoLogistic.isVisible = true
            badgeDoLogistic.number = it
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
        refreshBadgeValue()
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
            R.id.beranda_sv_pm, R.id.beranda_logistic, R.id.beranda_purchasing, R.id.beranda_admin_gudang -> {
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
                openActivity(DeliveryOrderActivity::class.java)
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
        if(storageViewModel.role == UserRole.LOGISTIC.name){
            berandaViewModel.kendaraanByLogistic.observe(this){
                if(it==null) {
                    binding.cvKendaraan.setGone()
                    binding.tvEmptyKendaraan.setVisible()
                }else{
                    binding.tvEmptyKendaraan.setGone()
                    binding.cvKendaraan.setVisible()
                    binding.tvGudangKendaraan.text = it.namaGudang
                    binding.tvAlamatKendaraan.text = it.alamatGudang
                    binding.tvMerkKendaraan.text = it.merk
                    binding.tvPlatKendaraan.text = it.platNomor
                    binding.ivKendaraan.setImageFromUrl(it.gambar, this)
                    when(it.jenis){
                        JenisKendaraan.MINIBUS.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_minibus)
                        JenisKendaraan.MOBIL.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_car)
                        JenisKendaraan.MOTOR.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_motorcycle)
                        JenisKendaraan.PICKUP.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_pickup)
                        JenisKendaraan.TRONTON.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_tronton)
                        JenisKendaraan.TRUCK.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_box)
                    }
                }
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
        if(storageViewModel.role==UserRole.ADMIN.name || storageViewModel.role==UserRole.ADMIN_GUDANG.name){
            berandaViewModel.statistikMenungguSuratJalan.observe(this){stat->
                adapterStatSJ = ListStatistikMenungguSuratJalanAdapter()
                adapterStatSJ.submitList(stat)
                binding.rvMenungguSuratJalan.layoutManager = GridLayoutManager(applicationContext,2,GridLayoutManager.VERTICAL,false)
                binding.rvMenungguSuratJalan.adapter = adapterStatSJ
            }
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
            initCountTextAndButton(deliveryOrder.count!!,binding.tvCountDeliveryOrder,binding.btnSeeAllDeliveryOrder)
        }
        initAppBar()
        initLayout()
        initUi()
    }
    private fun initCountTextAndButton(count: Int, tvCount: TextView, btn: Button? = null){
        tvCount.text = count.toString()
        if(count>0){
            tvCount.maxWidth = 240
            tvCount.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            btn?.setVisible()
        }
    }
    private fun initAppBar(){
        binding.tvNamaUser.text = storageViewModel.name
        binding.tvRoleUser.text = enumValueToNormal(storageViewModel.role)
        if(storageViewModel.photo.isNotBlank()){
            binding.ivUser.setImageFromUrl(storageViewModel.photo, applicationContext)
        }
        binding.ivUser.setOnClickListener{
            openActivity(ProfileActivity::class.java, false)
        }
    }
    private fun initLayout(){
        if(storageViewModel.role == UserRole.LOGISTIC.name ||
            storageViewModel.role == UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role == UserRole.ADMIN.name ||
            storageViewModel.role == UserRole.PURCHASING.name
        ) {
            binding.layoutDeliveryOrder.setVisible()
            binding.layoutDoDalamPerjalanan.setVisible()
        }
        if(storageViewModel.role == UserRole.LOGISTIC.name ||
            storageViewModel.role == UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role == UserRole.ADMIN.name ||
            storageViewModel.role == UserRole.SUPERVISOR.name ||
            storageViewModel.role == UserRole.PROJECT_MANAGER.name ||
            storageViewModel.role == UserRole.SITE_MANAGER.name
        ) {
            binding.layoutSjPengembalian.setVisible()
            binding.layoutSjPengirimanGp.setVisible()
            binding.layoutSjPengirimanPp.setVisible()
            binding.layoutSjDalamPerjalanan.setVisible()
        }
        if(storageViewModel.role == UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role == UserRole.ADMIN.name
        ) {
            binding.layoutMenungguSuratJalan.setVisible()
        }
        if(storageViewModel.role == UserRole.LOGISTIC.name
        ) {
            binding.layoutKendaraan.setVisible()
        }
    }
    private fun initAdapterDeliveryOrder(): ListDeliveryOrderAdapter{
        return ListDeliveryOrderAdapter(storageViewModel.role, storageViewModel.userId){
            openActivityWithExtras(DeliveryOrderDetailActivity::class.java,false) {
                putString(Constant.INTENT_ID, it.id)
            }
        }
    }
    private fun initAdapterSj(): ListSuratJalanAdapter{
        return ListSuratJalanAdapter(storageViewModel.role, storageViewModel.userId) {
            openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                putString(SuratJalanDetailActivity.ID_SURAT_JALAN, it.id)
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
        with(binding){
            srLayout.setOnRefreshListener {
                berandaViewModel.getAllData()
                refreshBadgeValue()
            }
            btnSeeAllDeliveryOrder.setOnClickListener{
                openActivity(DeliveryOrderActivity::class.java)
            }
            btnSeeAllSjPengembalian.setOnClickListener{
                openActivity(SuratJalanActivity::class.java)
            }
            btnSeeAllSjPengirimanGp.setOnClickListener{
                openActivity(SuratJalanActivity::class.java)
            }
            btnSeeAllSjPengirimanPp.setOnClickListener{
                openActivity(SuratJalanActivity::class.java)
            }
        }
    }
}