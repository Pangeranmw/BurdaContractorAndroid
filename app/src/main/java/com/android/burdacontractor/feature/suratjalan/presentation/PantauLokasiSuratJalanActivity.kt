package com.android.burdacontractor.feature.suratjalan.presentation


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.ActivityPantauLokasiSuratJalanBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
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

open class PantauLokasiSuratJalanActivity : Activity(), MapEventsReceiver, LocationListener,
    SensorEventListener {
    private lateinit var binding: ActivityPantauLokasiSuratJalanBinding
    private lateinit var map: MapView
    private var startPoint: GeoPoint = GeoPoint(-8.573884, 116.091460)
    private var destinationPoint: GeoPoint = GeoPoint(-8.579930, 116.100290)
    private lateinit var mItineraryMarkers: FolderOverlay

    private var markerStart: Marker? = null
    private var markerDestination: Marker? =null
    private lateinit var myLocationOverlay: DirectedLocationOverlay
    private lateinit var mLocationManager: LocationManager

    private var mTrackingMode = false
    private var mAzimuthAngleSpeed = 0.0f
    private var mSelectedRoad = 0
    private var mRoadOverlays: Array<Polyline?>? = null
    private lateinit var mRoadNodeMarkers: FolderOverlay
    private var markerInfoWindow: MarkerInfoWindow? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantauLokasiSuratJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            map.zoomToBoundingBox(getBoundingBox(startPoint, destinationPoint), false, 100)
            map.invalidate()
        }

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

        // Itinerary markers:
        mItineraryMarkers = FolderOverlay()
        mItineraryMarkers.name = getString(R.string.itinerary_markers_title)
        map.overlays.add(mItineraryMarkers)

        markerInfoWindow = MarkerInfoWindow(R.layout.itinerary_bubble, map)

        //Tracking system:
        binding.btnTrackingMode.setOnClickListener {
            mTrackingMode = !mTrackingMode
            updateUIWithTrackingMode()
        }

        //context menu for clicking on the map is registered on this button.
        //(a little bit strange, but if we register it on mapView, it will catch map drag events)

        //Route and Directions
        mRoadNodeMarkers = FolderOverlay()
        mRoadNodeMarkers.name = "Route Steps"
        map.overlays.add(mRoadNodeMarkers)
        roadAsync
        checkPermissions()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        if (resultCode == RESULT_OK) {
            val nodeId = intent.getIntExtra("NODE_ID", 0)
            map.controller.setCenter(mRoads!![mSelectedRoad].mNodes[nodeId].mLocation)
            val roadMarker = mRoadNodeMarkers.items[nodeId] as Marker
            roadMarker.showInfoWindow()
        }
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
            binding.btnTrackingMode.setBackgroundResource(R.drawable.ic_tracking_on)
            if (myLocationOverlay.isEnabled && myLocationOverlay.location != null) {
                map.controller.animateTo(myLocationOverlay.location)
            }
            map.mapOrientation = -mAzimuthAngleSpeed
            binding.btnTrackingMode.keepScreenOn = true
        } else {
            binding.btnTrackingMode.setBackgroundResource(R.drawable.ic_tracking_off)
            map.mapOrientation = 0.0f
            binding.btnTrackingMode.keepScreenOn = false
        }
    }
    private fun updateItineraryMarker(p: GeoPoint?,
        titleResId: Int, markerResId: Int, snippet: String?
    ): Marker {
        val marker = Marker(map)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.infoWindow = markerInfoWindow
        marker.isDraggable = true
        mItineraryMarkers.add(marker)
        val title = resources.getString(titleResId)
        marker.title = title
        marker.position = p
        val icon = ResourcesCompat.getDrawable(resources, markerResId, null)
        marker.icon = icon
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.invalidate()
        if (snippet != null) marker.snippet = snippet
        return marker
}

    private fun updateUIWithItineraryMarkers() {
        mItineraryMarkers.closeAllInfoWindows()
        mItineraryMarkers.items.clear()
        markerStart = updateItineraryMarker(startPoint, R.string.departure, R.drawable.ic_asal, null)
        markerDestination = updateItineraryMarker(destinationPoint, R.string.destination, R.drawable.ic_tujuan, null)
    }
    private fun updateUIWithRoads(roads: Array<Road>?) {
        mRoadNodeMarkers.items.clear()
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
        for (i in roads.indices) {
            val roadColor = ContextCompat.getColor(this, R.color.purple_500)
            val roadPolyline = RoadManager.buildRoadOverlay(roads[i], roadColor, 15.0f)
            mRoadOverlays!![i] = roadPolyline
            mapOverlays.add(1, roadPolyline)
        }
        updateUIWithItineraryMarkers()
    }
    private fun updateRoadTask(vararg params: ArrayList<GeoPoint>){
        var road: Array<Road>
        CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.IO) {
                val waypoints = params[0]
                val roadManager: RoadManager
                roadManager = OSRMRoadManager(this@PantauLokasiSuratJalanActivity, userAgent)
                road = roadManager.getRoads(waypoints)
            }
            withContext(Dispatchers.Main){
                mRoads = road
                updateUIWithRoads(mRoads)
            }
        }
    }
    private val roadAsync: Unit
        get() {
            mRoads = null
            val waypoints = ArrayList<GeoPoint>(2)
            waypoints.add(startPoint)
            waypoints.add(destinationPoint)
            updateRoadTask(waypoints)
        }
    override fun longPressHelper(p: GeoPoint): Boolean {
        return true
    }

    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
        InfoWindow.closeAllInfoWindowsOn(map)
        return true
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


    //------------ SensorEventListener implementation
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