package com.android.burdacontractor.feature.deliveryorder.presentation.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getCoordinate
import com.android.burdacontractor.core.utils.getLatitude
import com.android.burdacontractor.core.utils.getLongitude
import com.android.burdacontractor.core.utils.parcelable
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.toCoordinateFormat
import com.android.burdacontractor.databinding.ActivityTelusuriLokasiDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.NetworkLocationIgnorer
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay

@AndroidEntryPoint
class TelusuriLokasiDeliveryOrderActivity : AppCompatActivity(), MapEventsReceiver, LocationListener, SensorEventListener {
    private lateinit var binding: ActivityTelusuriLokasiDeliveryOrderBinding
    private val telusuriLokasiDeliveryOrderViewModel: TelusuriLokasiDeliveryOrderViewModel by viewModels()
    private var deliveryOrder: DeliveryOrderDetailItem? = null
    private var snackbar: Snackbar? = null

    private lateinit var map: MapView
    private var driverPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private var destinationPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private var originPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private var markerStartPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private var markerDestinationPoint: GeoPoint = GeoPoint(0.0, 0.0)
    private lateinit var mItineraryMarkers: FolderOverlay

    private var markerStart: Marker? = null
    private var markerDestination: Marker? =null
    private lateinit var myLocationOverlay: DirectedLocationOverlay
    private lateinit var mLocationManager: LocationManager

    private var mTrackingMode = false
    private var mAzimuthAngleSpeed = 0.0f
    private var mRoadOverlays: Array<Polyline?>? = null
    private var markerInfoWindow: MarkerInfoWindow? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelusuriLokasiDeliveryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deliveryOrder = intent.parcelable(Constant.INTENT_PARCEL)
        initMap()

        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        telusuriLokasiDeliveryOrderViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }
    }
    private fun initObserver(){
        telusuriLokasiDeliveryOrderViewModel.state.observe(this){
            when(it){
                StateResponse.LOADING -> binding.progressBar.isVisible = true
                StateResponse.ERROR -> binding.progressBar.isVisible = false
                StateResponse.SUCCESS -> {
                    binding.progressBar.isVisible = false
                }
                else -> {}
            }
        }
        telusuriLokasiDeliveryOrderViewModel.distanceMatrix.observe(this){
            val duration = it.routes[0].duration
            val durationConvert = when(it.routes[0].duration.toInt()){
                in 0..60 -> String.format("%.2f Dtk", duration)
                in 60..3600 -> String.format("%.2f Mnt", duration/60)
                else -> String.format("%.2f Jam", duration/3600)
            }
            val distance = it.routes[0].distance
            val distanceConvert = when(it.routes[0].distance.toInt()){
                in 0..1000 -> String.format("%.2f Mtr", distance)
                else -> String.format("%.2f Km", distance/1000)
            }
            binding.tvJarakValue.text = distanceConvert
            binding.tvWaktuValue.text = durationConvert
        }
        initUi()
    }
    private fun initMap(){
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Configuration.getInstance().userAgentValue = userAgent

        if(deliveryOrder!=null){
            val startLat = deliveryOrder!!.gudang.coordinate.getLatitude()
            val startLong = deliveryOrder!!.gudang.coordinate.getLongitude()
            val desLat = deliveryOrder!!.perusahaan.coordinate.getLatitude()
            val desLong = deliveryOrder!!.perusahaan.coordinate.getLongitude()
            destinationPoint = GeoPoint(desLat, desLong)
            originPoint = GeoPoint(startLat, startLong)
            markerStartPoint = GeoPoint(startLat, startLong)
            markerDestinationPoint = GeoPoint(desLat, desLong)
        }
        map = binding.map
        map.isTilesScaledToDpi = true
        map.setMultiTouchControls(true)
        map.minZoomLevel = 1.0
        map.maxZoomLevel = 21.0
        map.isVerticalMapRepetitionEnabled = false

        //To use MapEventsReceiver methods, we add a MapEventsOverlay:
        val overlay = MapEventsOverlay(this)
        map.overlays.add(overlay)
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        map.addOnFirstLayoutListener { _, _, _, _, _ ->
            map.zoomToBoundingBox(getBoundingBox(driverPoint, destinationPoint), false, 100)
            map.invalidate()
        }

        // Itinerary markers:
        mItineraryMarkers = FolderOverlay()
        mItineraryMarkers.name = getString(R.string.itinerary_markers_title)
        map.overlays.add(mItineraryMarkers)

        markerInfoWindow = MarkerInfoWindow(R.layout.itinerary_bubble, map)

        myLocationOverlay = DirectedLocationOverlay(this)
        val navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_direction_tracking)
        myLocationOverlay.setDirectionArrow(navigationIcon!!.toBitmap())
        map.overlays.add(myLocationOverlay)

        var location: Location? = null
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) location =
                mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (location != null) {
            //location known:
            onLocationChanged(location)
        } else {
            //no location known: hide myLocationOverlay
            myLocationOverlay.isEnabled = false
        }

        updateUIWithItineraryMarkers()
        roadAsync
        checkPermissions()
    }
    private fun initUi(){
        with(binding){
            tvTrackingTo.text = getString(R.string.rute_menuju, deliveryOrder!!.perusahaan.nama)
            tvNamaDriver.text = deliveryOrder!!.logistic.nama
            tvNamaPurchasing.text = deliveryOrder!!.purchasing.nama
            tvRolePurchasing.text = enumValueToNormal(deliveryOrder!!.purchasing.role)
            ivDriver.setImageFromUrl(deliveryOrder!!.logistic.foto,this@TelusuriLokasiDeliveryOrderActivity)
            ivPurchasing.setImageFromUrl(deliveryOrder!!.purchasing.foto,this@TelusuriLokasiDeliveryOrderActivity)
            btnTrackingMode.setOnClickListener {
                mTrackingMode = !mTrackingMode
                updateUIWithTrackingMode()
            }
            btnTrackGmaps.setOnClickListener {
                goNavigate(destinationPoint.latitude,destinationPoint.longitude)
            }
            btnTrackOrigin.setOnClickListener {
                val tempOriginPoint = originPoint
                originPoint = destinationPoint
                destinationPoint = tempOriginPoint
                changeTrackingTo(
                    getString(R.string.rute_menuju, deliveryOrder?.gudang?.nama),
                    btnTrackOrigin,
                    btnTrackDestination
                )
            }
            btnTrackDestination.setOnClickListener {
                val tempDesPoint = destinationPoint
                destinationPoint = originPoint
                originPoint = tempDesPoint
                changeTrackingTo(
                    getString(R.string.rute_menuju, deliveryOrder?.perusahaan?.nama),
                    btnTrackDestination,
                    btnTrackOrigin
                )
            }
            btnZoomDriver.setOnClickListener {
                map.controller.animateTo(driverPoint, 18.0, null)
            }
            onBackPressedCallback()
        }
    }

    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }
    private fun changeTrackingTo(menuju: String, btnListener: ImageButton, btnActive: ImageButton){
        with(binding){
            val origin = toCoordinateFormat(driverPoint.latitude,driverPoint.longitude)
            val dest = toCoordinateFormat(destinationPoint.latitude, destinationPoint.longitude)
            telusuriLokasiDeliveryOrderViewModel.getDistanceMatrix(getCoordinate(origin,dest))

            tvTrackingTo.text = menuju
            btnListener.isEnabled = false
            btnListener.isClickable = false
            btnListener.setBackgroundResource(R.drawable.semi_rounded_primary)

            btnActive.setBackgroundResource(R.drawable.semi_rounded_white)
            btnActive.isEnabled = true
            btnActive.isClickable = true
            map.zoomToBoundingBox(getBoundingBox(driverPoint, destinationPoint), false, 100)
            roadAsync
            map.invalidate()
        }
    }
    private fun goNavigate(lat: Double, lon: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lon&mode=l")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
    private fun checkPermissions() {
        val permissions: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            val params = permissions.toTypedArray()
            ActivityCompat.requestPermissions(this, params,
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
        }
    }
    private fun getBoundingBox(start: GeoPoint, end: GeoPoint): BoundingBox {
        val north: Double
        val south: Double
        val east: Double
        val west: Double
        if (start.latitude > end.latitude) {
            north = start.latitude
            south = end.latitude
        } else {
            north = end.latitude
            south = start.latitude
        }
        if (start.longitude > end.longitude) {
            east = start.longitude
            west = end.longitude
        } else {
            east = end.longitude
            west = start.longitude
        }
        return BoundingBox(north, east, south, west)
    }

    private fun startLocationUpdates(): Boolean {
        var result = false
        for (provider in mLocationManager.getProviders(true)) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationManager.requestLocationUpdates(
                    provider,
                    (2 * 1000).toLong(),
                    0.0f,
                    this
                )
                result = true
            }
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        val isOneProviderEnabled = startLocationUpdates()
        myLocationOverlay.isEnabled = isOneProviderEnabled
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationManager.removeUpdates(this)
        }
    }

    private fun updateUIWithTrackingMode() {
        if (mTrackingMode) {
            binding.btnTrackingMode.setImageResource(R.drawable.ic_tracking_on)
            if (myLocationOverlay.isEnabled && myLocationOverlay.location != null) {
                map.zoomToBoundingBox(getBoundingBox(driverPoint,destinationPoint),false,100)
            }
            map.mapOrientation = -mAzimuthAngleSpeed
            binding.btnTrackingMode.keepScreenOn = true
        } else {
            binding.btnTrackingMode.setImageResource(R.drawable.ic_tracking_off)
            map.mapOrientation = 0.0f
            binding.btnTrackingMode.keepScreenOn = false
        }
    }
    private fun updateItineraryMarker(p: GeoPoint?,
                                      title: String, markerResId: Int, snippet: String?
    ): Marker {
        val marker = Marker(map)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.infoWindow = markerInfoWindow
        mItineraryMarkers.add(marker)
        marker.title = title
        marker.position = p
        val icon = ResourcesCompat.getDrawable(resources, markerResId, null)
        marker.icon = icon
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        if (snippet != null) marker.snippet = snippet
        map.invalidate()
        return marker
    }

    private fun updateUIWithItineraryMarkers() {
        mItineraryMarkers.closeAllInfoWindows()
        mItineraryMarkers.items.clear()
        markerStart = updateItineraryMarker(
            markerStartPoint,
            deliveryOrder!!.gudang.nama,
            R.drawable.marker_ic_gudang_location,
            deliveryOrder!!.gudang.alamat
        )
        markerDestination = updateItineraryMarker(
            markerDestinationPoint,
            deliveryOrder!!.perusahaan.nama,
            R.drawable.marker_ic_perusahaan_location,
            deliveryOrder!!.perusahaan.alamat
        )
    }
    private fun updateUIWithRoads(roads: Array<Road>?) {
        val mapOverlays = map.overlays
        if (mRoadOverlays != null) {
            for (i in mRoadOverlays!!.indices) mapOverlays.remove(mRoadOverlays!![i])
            mRoadOverlays = null
        }
        if (roads == null) return
        if (roads[0].mStatus == Road.STATUS_TECHNICAL_ISSUE) Toast.makeText(
            map.context,
            "Technical issue when getting the route",
            Toast.LENGTH_SHORT
        ).show() else if (roads[0].mStatus > Road.STATUS_TECHNICAL_ISSUE) //functional issues
            Toast.makeText(map.context, "No possible route here", Toast.LENGTH_SHORT).show()
        mRoadOverlays = arrayOfNulls(roads.size)
        val roadColor = ContextCompat.getColor(this, R.color.purple_500)
        val roadPolyline = RoadManager.buildRoadOverlay(roads[0], roadColor, 15.0f)
        mRoadOverlays!![0] = roadPolyline
        mapOverlays.add(1, roadPolyline)
    }
    private fun updateRoadTask(vararg params: ArrayList<GeoPoint>){
        var road: Array<Road>
        CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.IO) {
                val waypoints = params[0]
                val roadManager: RoadManager

                roadManager = OSRMRoadManager(this@TelusuriLokasiDeliveryOrderActivity, Configuration.getInstance().userAgentValue)
                road = roadManager.getRoads(waypoints)
            }
            withContext(Dispatchers.Main){
                binding.progressBar.setVisible()
                mRoads = road
                updateUIWithRoads(mRoads)
                binding.progressBar.setGone()
            }
        }
    }
    private val roadAsync: Unit
        get() {
            mRoads = null
            val waypoints = ArrayList<GeoPoint>(2)
            waypoints.add(driverPoint)
            waypoints.add(destinationPoint)
            updateRoadTask(waypoints)
        }
    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
        InfoWindow.closeAllInfoWindowsOn(map)
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }

    //------------ LocationListener implementation
    private val mIgnorer = NetworkLocationIgnorer()
    private var mLastTime: Long = 0 // milliseconds
    private var mSpeed = 0.0 // km/h
    override fun onLocationChanged(pLoc: Location) {
        val currentTime = System.currentTimeMillis()
        if (mIgnorer.shouldIgnore(pLoc.provider, currentTime)) return
        val dT = (currentTime - mLastTime).toDouble()
        if (dT < 100.0) {
            //Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
            return
        }
        mLastTime = currentTime
        val newLocation = GeoPoint(pLoc)
        driverPoint = newLocation
        val origin = toCoordinateFormat(driverPoint.latitude,driverPoint.longitude)
        val dest = toCoordinateFormat(destinationPoint.latitude, destinationPoint.longitude)
        telusuriLokasiDeliveryOrderViewModel.getDistanceMatrix(getCoordinate(origin,dest))
        if (!myLocationOverlay.isEnabled) {
            //we get the location for the first time:
            myLocationOverlay.isEnabled = true
            map.controller.animateTo(newLocation)
        }
        val prevLocation = myLocationOverlay.location
        myLocationOverlay.location = newLocation
        myLocationOverlay.setAccuracy(pLoc.accuracy.toInt())
        if (prevLocation != null && pLoc.provider == LocationManager.GPS_PROVIDER) {
            mSpeed = pLoc.speed * 3.6
            //TODO: check if speed is not too small
            if (mSpeed >= 0.1) {
                mAzimuthAngleSpeed = pLoc.bearing
                myLocationOverlay.setBearing(mAzimuthAngleSpeed)
            }
        }
        if (mTrackingMode) {
            //keep the map view centered on current location:
            map.controller.animateTo(newLocation)
            map.mapOrientation = -mAzimuthAngleSpeed
        } else {
            //just redraw the location overlay:
            map.invalidate()
        }
    }
    override fun onSensorChanged(event: SensorEvent?) { }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        myLocationOverlay.setAccuracy(accuracy)
        map.invalidate()
    }
    companion object {
        var mRoads : Array<Road>? = null
        const val userAgent = BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME
        const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    }
}