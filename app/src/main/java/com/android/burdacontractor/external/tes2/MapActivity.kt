//package com.android.burdacontractor.presentation.test.tes2
//
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.StrictMode
//import android.os.StrictMode.ThreadPolicy
//import android.preference.PreferenceManager
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.android.burdacontractor.presentation.test.tes2.customview.MarkerInfoWindowCustom
//import com.example.osmwithbackgroundlocation2.databinding.ActivityMapBinding
//import org.osmdroid.bonuspack.routing.OSRMRoadManager
//import org.osmdroid.bonuspack.routing.Road
//import org.osmdroid.bonuspack.routing.RoadManager
//import org.osmdroid.bonuspack.routing.RoadNode
//import org.osmdroid.config.Configuration.getInstance
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory
//import org.osmdroid.util.BoundingBox
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.util.NetworkLocationIgnorer
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.Marker
//import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay
//
//
//class MapActivity : AppCompatActivity(), LocationListener {
//    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
//    private lateinit var map : MapView
//    private lateinit var binding: ActivityMapBinding
//    private var mIgnorer = NetworkLocationIgnorer()
//    private var mLastTime = System.currentTimeMillis()
//    private var mSpeed = 0.0
//    private var mAzimuthAngleSpeed = 0f
//    private var mTrackingMode = true
//    private var mIsRecordingTrack = true
//    private lateinit var myLocationOverlay: DirectedLocationOverlay
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMapBinding.inflate(layoutInflater)
//
//        val policy = ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
//
//        getInstance().userAgentValue = "MyOwnUserAgent/1.0"
//
//        //handle permissions first, before map is created. not depicted here
//
//        //load/initialize the osmdroid configuration, this can be done
//        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
//        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
//        //setting this before the layout is inflated is a good idea
//        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
//        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
//        //see also StorageUtils
//        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
//        //tile servers will get you banned based on this string.
//
//        //inflate and create the map
//        setContentView(binding.root)
//
//        map = binding.map
//        map.setTileSource(TileSourceFactory.MAPNIK)
//        map.setMultiTouchControls(true)
//
//        val mapController = map.controller
//        mapController.setZoom(21.5)
//        map.maxZoomLevel = 22.5
//        val startPoint = GeoPoint(-6.212401, 106.702645)
//        mapController.setCenter(startPoint)
//
//        val roadManager: RoadManager = OSRMRoadManager(this, getInstance().userAgentValue)
//        // set start and end point
//        val waypoints = ArrayList<GeoPoint>()
//        waypoints.add(startPoint)
//        val endPoint = GeoPoint(-6.2483, 106.8666)
//        waypoints.add(endPoint);
//        // And retreive the road between those points:
//        val road: Road = roadManager.getRoad(waypoints)
////        then, build a Polyline with the route shape:
//        val roadColor = ContextCompat.getColor(this, R.color.red);
//        val roadOverlay = RoadManager.buildRoadOverlay(road, roadColor, 20.0f)
////        Add this Polyline to the overlays of your map:
//        map.overlays.add(roadOverlay)
//        map.invalidate()
//        (roadManager as OSRMRoadManager).setMean(OSRMRoadManager.MEAN_BY_CAR)
//
//        val markerStartBitmap = ContextCompat.getDrawable(this, R.drawable.vehicle_car)?.setScale(1)
//        val markerStart = BitmapDrawable(resources, markerStartBitmap)
//        val markerDestination = ContextCompat.getDrawable(this, R.drawable.marker_ic_gudang_location)
//
//        val startPointIndex = road.mNodes.indices.first
//        val endPointIndex = road.mNodes.indices.last
//
//        setRoadNodeMarker(road.mNodes[startPointIndex], markerStart, "Driver")
//        setRoadNodeMarker(road.mNodes[endPointIndex], markerDestination, "Gudang")
//
//        map.addOnFirstLayoutListener { _, _, _, _, _ ->
//            map.zoomToBoundingBox(getBoundingBox(startPoint, endPoint), true, 100)
//            map.invalidate()
//        }
//    }
//
////    private fun setRoadNodeMarkerWithInstruction(){
////        val markerInstruction = ContextCompat.getDrawable(this, R.drawable.ic_navigation)
////        for (i in road.mNodes.indices) {
////            val node = road.mNodes[i]
////            val nodeMarker = Marker(map)
////            val title = "Step $i"
////            val snippet = node.mInstructions
////
////            val subDescription = Road.getLengthDurationText(this, node.mLength, node.mDuration)
////            val infoWindow = MarkerInfoWindow(map, title, snippet, subDescription)
////            nodeMarker.infoWindow = infoWindow
////
//////            infoWindow.subDescription = Road.getLengthDurationText(this, node.mLength, node.mDuration)
//////            infoWindow.snippet = node.mInstructions
////
////            nodeMarker.position = node.mLocation
////            nodeMarker.icon = if(i==0) markerStart else markerInstruction
//////            nodeMarker.title = "Step $i"
////            map.overlays.add(nodeMarker)
////            if (i == road.mNodes.indices.last) {
////                nodeMarker.icon = markerDestination
////            }
////        }
////    }
//
//    override fun onResume() {
//        super.onResume()
//        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
//    }
//
//    override fun onPause() {
//        super.onPause()
//        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        val permissionsToRequest = ArrayList<String>()
//        var i = 0
//        while (i < grantResults.size) {
//            permissionsToRequest.add(permissions[i])
//            i++
//        }
//        if (permissionsToRequest.size > 0) {
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsToRequest.toTypedArray(),
//                REQUEST_PERMISSIONS_REQUEST_CODE)
//        }
//    }
//
//    private fun setRoadNodeMarker(roadNode: RoadNode, icon: Drawable? = null, title: String? = null, snippet: String? = null){
//        val roadNodeMarker = Marker(map)
//        val snippetValue = if(snippet.isNullOrEmpty()) roadNode.mInstructions else snippet
//        val subDescription = Road.getLengthDurationText(this, roadNode.mLength, roadNode.mDuration)
//        val infoWindow = MarkerInfoWindowCustom(map, title, snippetValue, subDescription)
//        roadNodeMarker.infoWindow = infoWindow
//        roadNodeMarker.position = roadNode.mLocation
//        roadNodeMarker.icon = icon
//        map.overlays.add(roadNodeMarker)
//    }
//
//    private fun Drawable.setScale(multiplySize: Int): Bitmap{
//        val bitmap = Bitmap.createBitmap(
//            this.intrinsicWidth,
//            this.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        this.setBounds(0, 0, canvas.width*multiplySize, canvas.height*multiplySize)
//        this.draw(canvas)
//        return bitmap
//    }
//
//    private fun getBoundingBox(start: GeoPoint, end: GeoPoint): BoundingBox? {
//        val north: Double
//        val south: Double
//        val east: Double
//        val west: Double
//        if (start.latitude > end.latitude) {
//            north = start.latitude
//            south = end.latitude
//        } else {
//            north = end.latitude
//            south = start.latitude
//        }
//        if (start.longitude > end.longitude) {
//            east = start.longitude
//            west = end.longitude
//        } else {
//            east = end.longitude
//            west = start.longitude
//        }
//        return BoundingBox(north, east, south, west)
//    }
//
//    override
//    fun onLocationChanged(pLoc: Location) {
//        val currentTime = System.currentTimeMillis()
//        if (mIgnorer.shouldIgnore(pLoc.provider, currentTime)) return
//        val dT: Double = currentTime - mLastTime.toDouble()
//        if (dT < 100.0) {
//            //Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
//            return
//        }
//        mLastTime = currentTime
//        val newLocation = GeoPoint(pLoc)
//        if (!myLocationOverlay.isEnabled) {
//            //we get the location for the first time:
//            myLocationOverlay.isEnabled = true
//            map.controller.animateTo(newLocation)
//        }
//        val prevLocation: GeoPoint = myLocationOverlay.location
//        myLocationOverlay.location = newLocation
//        myLocationOverlay.setAccuracy(pLoc.accuracy as Int)
//        if (prevLocation != null && pLoc.provider.equals(LocationManager.GPS_PROVIDER)) {
//            mSpeed = pLoc.speed * 3.6
////            val speedInt = mSpeed.roundToInt()
////            val speedTxt = findViewById<TextView>(R.id.)
////            speedTxt.text = "$speedInt km/h"
//
//            //TODO: check if speed is not too small
//            if (mSpeed >= 0.1) {
//                mAzimuthAngleSpeed = pLoc.bearing
//                myLocationOverlay.setBearing(mAzimuthAngleSpeed)
//            }
//        }
//        if (mTrackingMode) {
//            //keep the map view centered on current location:
//            map.controller.animateTo(newLocation)
//            map.mapOrientation = -mAzimuthAngleSpeed
//        } else {
//            //just redraw the location overlay:
//            map.invalidate()
//        }
//        if (mIsRecordingTrack) {
//            recordCurrentLocationInTrack("my_track", "My Track", newLocation)
//        }
//    }
//
//    private fun recordCurrentLocationInTrack(tag: String, tag2: String, newLocation: GeoPoint) {
//        Log.d(tag, tag2 + newLocation)
//    }
//
//    companion object{
//        const val TAG = "MapsActivity"
//    }
//}