package com.android.burdacontractor.feature.kendaraan.presentation.pantau

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.PinPoint
import com.android.burdacontractor.core.presentation.customview.MarkerInfoWindowAdapter
import com.android.burdacontractor.core.utils.BitmapHelper
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.setToastShort
import com.android.burdacontractor.databinding.ActivityPantauLokasiPengendaraBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PantauLokasiPengendaraActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPantauLokasiPengendaraBinding
    private val pantauLokasiPengendaraViewModel: PantauLokasiPengendaraViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private var mapView: MapView? = null
    private var driverMarker: Marker? = null
    private var isTrackDriver: Boolean = false
    private var driverLocation: LatLng? = null
    private var logisticId: String? = null
    private var namaPengendara: String? = null
    private var namaKendaraan: String? = null
    private val driverMarkerIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.primary_main)
        BitmapHelper.vectorToBitmap(this, R.drawable.marker_ic_driver_location, color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantauLokasiPengendaraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        logisticId = intent.getStringExtra(Constant.INTENT_ID)
        namaPengendara = intent.getStringExtra(NAMA_PENGENDARA)
        namaKendaraan = intent.getStringExtra(NAMA_KENDARAAN)
        pantauLokasiPengendaraViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) {
                mapView = binding.pinPointMap
                mapView?.onCreate(savedInstanceState)
                mapView?.getMapAsync(this)
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setToastShort("Notifications permission granted")
            } else {
                setToastShort("Notifications permission rejected")
            }
        }
    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-0.4740344, 112.409006), 5f))
        driverMarker = mMap.addMarker(
            MarkerOptions().position(mMap.cameraPosition.target).icon(driverMarkerIcon)
        )
        driverMarker?.tag = PinPoint(namaPengendara!!, namaKendaraan!!)
        pantauLokasiPengendaraViewModel.getLogisticActiveSjDoLocation(logisticId!!)
        pantauLokasiPengendaraViewModel.activeLocation.observe(this) {
            it?.let {
                it.forEach { data ->
                    val position = LatLng(data.latitude.toDouble(), data.longitude.toDouble())
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .title(data.kode)
                            .position(position)
                    )
                    when (data.tipe) {
                        "DO" -> {
                            val icon: BitmapDescriptor by lazy {
                                val color = ContextCompat.getColor(this, R.color.primary_main)
                                BitmapHelper.vectorToBitmap(
                                    this,
                                    R.drawable.marker_ic_delivery_order,
                                    color
                                )
                            }
                            marker?.setIcon(icon)
                        }

                        "SJGP", "SJPP" -> {
                            val icon: BitmapDescriptor by lazy {
                                val color = ContextCompat.getColor(this, R.color.primary_main)
                                BitmapHelper.vectorToBitmap(
                                    this,
                                    R.drawable.marker_ic_sj_pengiriman,
                                    color
                                )
                            }
                            marker?.setIcon(icon)
                        }

                        "SJPG" -> {
                            val icon: BitmapDescriptor by lazy {
                                val color = ContextCompat.getColor(this, R.color.primary_main)
                                BitmapHelper.vectorToBitmap(
                                    this,
                                    R.drawable.marker_ic_sj_pengembalian,
                                    color
                                )
                            }
                            marker?.setIcon(icon)
                        }
                    }
                    marker?.tag = PinPoint(data.nama, data.kode)
                }
            }
        }
        pantauLokasiPengendaraViewModel.getCoordinate(logisticId!!)
        pantauLokasiPengendaraViewModel.logisticCoordinate.observe(this) {
            if (it != null) {
                driverLocation = LatLng(it.latitude, it.longitude)
                driverMarker?.position = driverLocation!!
                if (isTrackDriver)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation!!, 20f))
            }
        }
        onBackPressedCallback()
        binding.btnTrackDriver.setOnCheckedChangeListener { _, b ->
            isTrackDriver = b
            if (b && driverLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation!!, 20f))
            }
        }
        getMyLocation()
    }

    fun onBackPressedCallback() {
        val finishAction = {
            finish()
            overridePendingTransition(0, 0)
        }
        binding.btnBack.setOnClickListener {
            finishAction()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAction()
            }
        })
    }

    companion object {
        const val NAMA_PENGENDARA = "namaPengendara"
        const val NAMA_KENDARAAN = "namaKendaraan"
    }
}