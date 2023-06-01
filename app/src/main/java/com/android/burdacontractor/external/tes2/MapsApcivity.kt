//package com.android.burdacontractor.presentation.test.tes2
//
//
////import org.osmdroid.bonuspack.location.GeocoderGraphHopper;
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import android.preference.PreferenceManager
//import android.view.ContextMenu
//import android.view.ContextMenu.ContextMenuInfo
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.View
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.res.ResourcesCompat
//import androidx.core.graphics.drawable.toBitmap
//import com.android.burdacontractor.presentation.test.tes2.customview.ViaPointInfoWindow
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.osmdroid.bonuspack.kml.KmlPlacemark
//import org.osmdroid.bonuspack.kml.KmlTrack
//import org.osmdroid.bonuspack.kml.LineStyle
//import org.osmdroid.bonuspack.kml.Style
//import org.osmdroid.bonuspack.location.GeocoderNominatim
//import org.osmdroid.bonuspack.routing.OSRMRoadManager
//import org.osmdroid.bonuspack.routing.Road
//import org.osmdroid.bonuspack.routing.RoadManager
//import org.osmdroid.config.Configuration
//import org.osmdroid.events.MapEventsReceiver
//import org.osmdroid.util.BoundingBox
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.util.NetworkLocationIgnorer
//import org.osmdroid.views.MapView
//import org.osmdroid.views.MapView.OnFirstLayoutListener
//import org.osmdroid.views.overlay.*
//import org.osmdroid.views.overlay.Marker.OnMarkerDragListener
//import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
//import org.osmdroid.views.overlay.infowindow.InfoWindow
//import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
//import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay
//import java.io.IOException
//
//class MapsActivity : Activity(), MapEventsReceiver, LocationListener,
//    SensorEventListener, OnFirstLayoutListener {
//    private lateinit var map: MapView
//    private var startPoint: GeoPoint? = GeoPoint(-8.573884, 116.091460)
//    private var destinationPoint: GeoPoint? = GeoPoint(-8.579930, 116.100290)
//    private lateinit var mItineraryMarkers: FolderOverlay
//
//    //for departure, destination and viapoints
//    private var markerStart: Marker? = null
//    private var markerDestination: Marker? =null
//    private lateinit var myLocationOverlay: DirectedLocationOverlay
//
//    //MyLocationNewOverlay myLocationNewOverlay;
//    private lateinit var mLocationManager: LocationManager
//
//    //private SensorManager mSensorManager;
//    //private Sensor mOrientation;
//    private var mTrackingMode = false
//    lateinit var mTrackingModeButton: Button
//    var mAzimuthAngleSpeed = 0.0f
//    lateinit var mDestinationPolygon : Polygon
//    private var mSelectedRoad = 0
//    private var mRoadOverlays: Array<Polyline?>? = null
//    private lateinit var mRoadNodeMarkers: FolderOverlay
//    protected var mViaPointInfoWindow: ViaPointInfoWindow? = null
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
//        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val v: View = inflater.inflate(R.layout.activity_maps, null)
//        setContentView(v)
//        map = v.findViewById<View>(R.id.map) as MapView
//        map.isTilesScaledToDpi = true
//        map.setMultiTouchControls(true)
//        map.minZoomLevel = 1.0
//        map.maxZoomLevel = 21.0
//        map.isVerticalMapRepetitionEnabled = false
//        val mapController = map.controller
//
//        //To use MapEventsReceiver methods, we add a MapEventsOverlay:
//        val overlay = MapEventsOverlay(this)
//        map.overlays.add(overlay)
//        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//
//        //map prefs:
//        mapController.setZoom(20.0)
//        val firstPoint = GeoPoint(-8.573884, 116.091460)
//        mapController.setCenter(firstPoint)
//
//        myLocationOverlay = DirectedLocationOverlay(this)
//        val navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_direction_tracking)
//        myLocationOverlay.setDirectionArrow(navigationIcon!!.toBitmap())
//        map.overlays.add(myLocationOverlay)
//
//        var location: Location? = null
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            if (location == null) location =
//                mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        }
//        if (location != null) {
//            //location known:
//            onLocationChanged(location)
//        } else {
//            //no location known: hide myLocationOverlay
//            myLocationOverlay.isEnabled = false
//        }
//
//        // Itinerary markers:
//        mItineraryMarkers = FolderOverlay()
//        mItineraryMarkers.name = getString(R.string.itinerary_markers_title)
//        map.overlays.add(mItineraryMarkers)
//
//        mViaPointInfoWindow = ViaPointInfoWindow(R.layout.itinerary_bubble, map)
//        updateUIWithItineraryMarkers()
//
//        //Tracking system:
//        mTrackingModeButton = findViewById<View>(R.id.buttonTrackingMode) as Button
//        mTrackingModeButton.setOnClickListener {
//            mTrackingMode = !mTrackingMode
//            updateUIWithTrackingMode()
//        }
//
//        //context menu for clicking on the map is registered on this button.
//        //(a little bit strange, but if we register it on mapView, it will catch map drag events)
//
//        //Route and Directions
//        mRoadNodeMarkers = FolderOverlay()
//        mRoadNodeMarkers.name = "Route Steps"
//        map.overlays.add(mRoadNodeMarkers)
//        if (savedInstanceState != null) {
//            //STATIC mRoad = savedInstanceState.getParcelable("road");
//            updateUIWithRoads(mRoads)
//        }
//
//        checkPermissions()
//    }
//
//    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
//    private fun checkPermissions() {
//        val permissions: MutableList<String> = ArrayList()
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//        if (permissions.isNotEmpty()) {
//            val params = permissions.toTypedArray()
//            ActivityCompat.requestPermissions(this, params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
//        }
//    }
//
//    /*
//	@Override
//	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//		switch (requestCode) {
//			case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//				Map<String, Integer> perms = new HashMap<>();
//				// Initial
//				perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//				// Fill with results
//				for (int i = 0; i < permissions.length; i++)
//					perms.put(permissions[i], grantResults[i]);
//			}
//		}
//	}
//	*/
//    private fun setViewOn(bb: BoundingBox?) {
//        if (bb != null) {
//            map.zoomToBoundingBox(bb, true)
//        }
//    }
//
//    //--- Stuff for setting the mapview on a box at startup:
//    private var mInitialBoundingBox: BoundingBox? = null
//    fun setInitialViewOn(bb: BoundingBox?) {
//        if (map.getScreenRect(null).height() == 0) {
//            mInitialBoundingBox = bb
//            map.addOnFirstLayoutListener(this)
//        } else map.zoomToBoundingBox(bb, false)
//    }
//
//    override fun onFirstLayout(v: View, left: Int, top: Int, right: Int, bottom: Int) {
//        if (mInitialBoundingBox != null) map.zoomToBoundingBox(mInitialBoundingBox, false)
//    }
//
//    /**
//     * callback to store activity status before a restart (orientation change for instance)
//     */
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable("location", myLocationOverlay.location)
//        outState.putBoolean("tracking_mode", mTrackingMode)
//        outState.putParcelable("start", startPoint)
//        outState.putParcelable("destination", destinationPoint)
////        savePrefs()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
//        if (resultCode == RESULT_OK) {
//            val nodeId = intent.getIntExtra("NODE_ID", 0)
//            map.controller.setCenter(mRoads!![mSelectedRoad].mNodes[nodeId].mLocation)
//            val roadMarker = mRoadNodeMarkers.items[nodeId] as Marker
//            roadMarker.showInfoWindow()
//        }
//    }
//
//    private fun startLocationUpdates(): Boolean {
//        var result = false
//        for (provider in mLocationManager.getProviders(true)) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                mLocationManager.requestLocationUpdates(
//                    provider,
//                    (2 * 1000).toLong(),
//                    0.0f,
//                    this
//                )
//                result = true
//            }
//        }
//        return result
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val isOneProviderEnabled = startLocationUpdates()
//        myLocationOverlay.isEnabled = isOneProviderEnabled
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mLocationManager.removeUpdates(this)
//        }
//        //TODO: mSensorManager.unregisterListener(this);
////        mFriendsManager.onPause()
////        savePrefs()
//    }
//
//    private fun updateUIWithTrackingMode() {
//        if (mTrackingMode) {
//            mTrackingModeButton.setBackgroundResource(R.drawable.ic_tracking_on)
//            if (myLocationOverlay.isEnabled && myLocationOverlay.location != null) {
//                map.controller.animateTo(myLocationOverlay.location)
//            }
//            map.mapOrientation = -mAzimuthAngleSpeed
//            mTrackingModeButton.keepScreenOn = true
//        } else {
//            mTrackingModeButton.setBackgroundResource(R.drawable.ic_tracking_off)
//            map.mapOrientation = 0.0f
//            mTrackingModeButton.keepScreenOn = false
//        }
//    }
//    //------------- Geocoding and Reverse Geocoding
//    /**
//     * Reverse Geocoding
//     */
//    private fun getAddress(p: GeoPoint): String {
//        val geocoder = GeocoderNominatim(userAgent)
//        //GeocoderGraphHopper geocoder = new GeocoderGraphHopper(Locale.getDefault(), graphHopperApiKey);
//        val theAddress: String? = try {
//            val dLatitude = p.latitude
//            val dLongitude = p.longitude
//            val addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1)
//            val sb = StringBuilder()
//            if (addresses.size > 0) {
//                val address = addresses[0]
//                val n = address.maxAddressLineIndex
//                for (i in 0..n) {
//                    if (i != 0) sb.append(", ")
//                    sb.append(address.getAddressLine(i))
//                }
//                sb.toString()
//            } else {
//                null
//            }
//        } catch (e: IOException) {
//            null
//        }
//        return theAddress ?: ""
//    }
//
//
////    private inner class GeocodingTask :
////        AsyncTask<Any?, Void?, List<Address>?>() {
////        var mIndex = 0
////        private override fun doInBackground(vararg params: Any): List<Address>? {
////            val locationAddress = params[0] as String
////            mIndex = params[1] as Int
////            val geocoder = GeocoderNominatim(userAgent)
////            geocoder.setOptions(true) //ask for enclosing polygon (if any)
////            //GeocoderGraphHopper geocoder = new GeocoderGraphHopper(Locale.getDefault(), graphHopperApiKey);
////            return try {
////                val viewbox = map.boundingBox
////                geocoder.getFromLocationName(
////                    locationAddress, 1,
////                    viewbox.latSouth, viewbox.lonEast,
////                    viewbox.latNorth, viewbox.lonWest, false
////                )
////            } catch (e: Exception) {
////                null
////            }
////        }
////
////        override fun onPostExecute(foundAdresses: List<Address>?) {
////            if (foundAdresses == null) {
////                Toast.makeText(applicationContext, "Geocoding error", Toast.LENGTH_SHORT).show()
////            } else if (foundAdresses.size == 0) { //if no address found, display an error
////                Toast.makeText(applicationContext, "Address not found.", Toast.LENGTH_SHORT).show()
////            } else {
////                val address = foundAdresses[0] //get first address
////                val addressDisplayName = address.extras.getString("display_name")
////                if (mIndex == START_INDEX) {
////                    startPoint = GeoPoint(address.latitude, address.longitude)
////                    markerStart = updateItineraryMarker(
////                        markerStart, startPoint, START_INDEX,
////                        R.string.departure, R.drawable.marker_departure, -1, addressDisplayName
////                    )
////                    map.controller.setCenter(startPoint)
////                } else if (mIndex == DEST_INDEX) {
////                    destinationPoint = GeoPoint(address.latitude, address.longitude)
////                    markerDestination = updateItineraryMarker(
////                        markerDestination, destinationPoint, DEST_INDEX,
////                        R.string.destination, R.drawable.marker_destination, -1, addressDisplayName
////                    )
////                    map.controller.setCenter(destinationPoint)
////                }
////                roadAsync
////                //get and display enclosing polygon:
////                val extras = address.extras
////                if (extras != null && extras.containsKey("polygonpoints")) {
////                    val polygon = extras.getParcelableArrayList<GeoPoint>("polygonpoints")
////                    //Log.d("DEBUG", "polygon:"+polygon.size());
////                    updateUIWithPolygon(polygon, addressDisplayName)
////                } else {
////                    updateUIWithPolygon(null, "")
////                }
////            }
////        }
////    }
////
////    /**
////     * Geocoding of the departure or destination address
////     */
////    fun handleSearchButton(index: Int, editResId: Int) {
////        val locationEdit = findViewById<View>(editResId) as EditText
////        //Hide the soft keyboard:
////        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
////        imm.hideSoftInputFromWindow(locationEdit.windowToken, 0)
////        val locationAddress = locationEdit.text.toString()
////        if (locationAddress == "") {
////            removePoint(index)
////            map.invalidate()
////            return
////        }
////        Toast.makeText(this, "Searching:\n$locationAddress", Toast.LENGTH_LONG).show()
////        AutoCompleteOnPreferences.storePreference(
////            this,
////            locationAddress,
////            SHARED_PREFS_APPKEY,
////            PREF_LOCATIONS_KEY
////        )
////        MapActivity.GeocodingTask().execute(locationAddress, index)
////    }
//
//    //add or replace the polygon overlay
//    fun updateUIWithPolygon(polygon: ArrayList<GeoPoint>?, name: String?) {
//        val mapOverlays = map.overlays
//        var location = -1
//        if (mDestinationPolygon != null) location = mapOverlays.indexOf(mDestinationPolygon)
//        mDestinationPolygon = Polygon()
//        mDestinationPolygon.fillColor = 0x15FF0080
//        mDestinationPolygon.strokeColor = -0x7fffff01
//        mDestinationPolygon.strokeWidth = 5.0f
//        mDestinationPolygon.title = name
//        var bb: BoundingBox? = null
//        if (polygon != null) {
//            mDestinationPolygon.points = polygon
//            bb = BoundingBox.fromGeoPoints(polygon)
//        }
//        if (location != -1) mapOverlays[location] = mDestinationPolygon else mapOverlays.add(
//            1,
//            mDestinationPolygon
//        ) //insert just above the MapEventsOverlay.
//        setViewOn(bb)
//        map.invalidate()
//    }
//
//    private fun reverseGeocodingTask(vararg params: Marker){
//        var marker: Marker?
//        var result: String?
//        CoroutineScope(Dispatchers.Main).launch{
//            withContext(Dispatchers.IO){
//                marker = params[0]
//                result = getAddress(marker!!.position)
//            }
//            withContext(Dispatchers.Main){
//                marker!!.snippet = result
//                marker!!.showInfoWindow()
//            }
//        }
//    }
////    //Async task to reverse-geocode the marker position in a separate thread:
////    private inner class ReverseGeocodingTask :
////        AsyncTask<Marker?, Void?, String>() {
////        var marker: Marker? = null
////        private override fun doInBackground(vararg params: Marker): String {
////            marker = params[0]
////            return getAddress(marker.position)
////        }
////
////        override fun onPostExecute(result: String) {
////            marker.snippet = result
////            marker.showInfoWindow()
////        }
////    }
//
//    //------------ Itinerary markers
//    inner class OnItineraryMarkerDragListener : OnMarkerDragListener {
//        override fun onMarkerDrag(marker: Marker) {}
//        override fun onMarkerDragEnd(marker: Marker) {
//            val index = marker.relatedObject as Int
//            if (index == START_INDEX)
//                startPoint = marker.position
//            else if (index == DEST_INDEX)
//                destinationPoint = marker.position
//            //update location:
//            reverseGeocodingTask(marker)
////            ReverseGeocodingTask().execute(marker)
//            //update route:
//            roadAsync
//        }
//
//        override fun onMarkerDragStart(marker: Marker) {}
//    }
//
//    private val mItineraryListener: OnItineraryMarkerDragListener =
//        OnItineraryMarkerDragListener()
//
//    /** Update (or create if null) a marker in itineraryMarkers.  */
//    private fun updateItineraryMarker(
//        valMarker: Marker?, p: GeoPoint?, index: Int,
//        titleResId: Int, markerResId: Int, imageResId: Int, address: String?
//    ): Marker {
//        var marker = valMarker
//        if (marker == null) {
//            marker = Marker(map)
//            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//            marker.infoWindow = mViaPointInfoWindow
//            marker.isDraggable = true
//            marker.setOnMarkerDragListener(mItineraryListener)
//            mItineraryMarkers.add(marker)
//        }
//        val title = resources.getString(titleResId)
//        marker.title = title
//        marker.position = p
//        val icon = ResourcesCompat.getDrawable(resources, markerResId, null)
//        marker.icon = icon
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//        if (imageResId != -1) marker.image =
//            ResourcesCompat.getDrawable(resources, imageResId, null)
//        marker.relatedObject = index
//        map.invalidate()
//        if (address != null) marker.snippet = address
//        else reverseGeocodingTask(marker)
//        return marker
//    }
//
//    fun removePoint(index: Int) {
//        if (index == START_INDEX) {
//            startPoint = null
//            if (markerStart != null) {
//                markerStart!!.closeInfoWindow()
//                mItineraryMarkers.remove(markerStart)
//                markerStart = null
//            }
//        } else if (index == DEST_INDEX) {
//            destinationPoint = null
//            if (markerDestination != null) {
//                markerDestination!!.closeInfoWindow()
//                mItineraryMarkers.remove(markerDestination)
//                markerDestination = null
//            }
//        }
//        roadAsync
//    }
//
//    fun updateUIWithItineraryMarkers() {
//        mItineraryMarkers.closeAllInfoWindows()
//        mItineraryMarkers.items.clear()
//        //Start marker:
//        if (startPoint != null) {
//            markerStart = updateItineraryMarker(
//                null, startPoint, START_INDEX,
//                R.string.departure, R.drawable.ic_asal, -1, null
//            )
//        }
//        //Destination marker if any:
//        if (destinationPoint != null) {
//            markerDestination = updateItineraryMarker(
//                null, destinationPoint, DEST_INDEX,
//                R.string.destination, R.drawable.ic_tujuan, -1, null
//            )
//        }
//    }
//
//    //------------ Route and Directions
//    private fun putRoadNodes(road: Road) {
//        mRoadNodeMarkers.items.clear()
//        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_navigation, null)
//        val n = road.mNodes.size
//        val infoWindow = MarkerInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map)
//        val iconIds = resources.obtainTypedArray(R.array.direction_icons)
//        for (i in 0 until n) {
//            val node = road.mNodes[i]
//            val instructions = if (node.mInstructions == null) "" else node.mInstructions
//            val nodeMarker = Marker(map)
//            nodeMarker.title = getString(R.string.step) + " " + (i + 1)
//            nodeMarker.snippet = instructions
//            nodeMarker.subDescription = Road.getLengthDurationText(this, node.mLength, node.mDuration)
//            nodeMarker.position = node.mLocation
//            nodeMarker.icon = icon
//            nodeMarker.setInfoWindow(infoWindow) //use a shared infowindow.
//            val iconId = iconIds.getResourceId(node.mManeuverType, R.drawable.ic_empty)
//            if (iconId != R.drawable.ic_empty) {
//                val image = ResourcesCompat.getDrawable(resources, iconId, null)
//                nodeMarker.image = image
//            }
//            nodeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
//            mRoadNodeMarkers.add(nodeMarker)
//        }
//        iconIds.recycle()
//    }
//
//     fun selectRoad(roadIndex: Int) {
//        mSelectedRoad = roadIndex
//        putRoadNodes(mRoads!![roadIndex])
//        //Set route info in the text view:
//        val textView = findViewById<View>(R.id.routeInfo) as TextView
//        textView.text = mRoads!![roadIndex].getLengthDurationText(this, -1)
//        for (i in mRoadOverlays!!.indices) {
//            val p = mRoadOverlays!![i]?.paint
//            if (i == roadIndex) p?.color = -0x7fffff01 //blue
//            else p?.color = -0x6f99999a //grey
//        }
//        map.invalidate()
//    }
//
//    internal inner class RoadOnClickListener : Polyline.OnClickListener {
//        override fun onClick(polyline: Polyline, mapView: MapView, eventPos: GeoPoint): Boolean {
//            val selectedRoad = polyline.relatedObject as Int
//            selectRoad(selectedRoad)
//            polyline.infoWindowLocation = eventPos
//            polyline.showInfoWindow()
//            return true
//        }
//    }
//
//    fun updateUIWithRoads(roads: Array<Road>?) {
//        mRoadNodeMarkers.items.clear()
//        val textView = findViewById<View>(R.id.routeInfo) as TextView
//        textView.text = ""
//        val mapOverlays = map.overlays
//        if (mRoadOverlays != null) {
//            for (i in mRoadOverlays!!.indices) mapOverlays.remove(mRoadOverlays!![i])
//            mRoadOverlays = null
//        }
//        if (roads == null) return
//        if (roads[0].mStatus == Road.STATUS_TECHNICAL_ISSUE) Toast.makeText(
//            map.context,
//            "Technical issue when getting the route",
//            Toast.LENGTH_SHORT
//        ).show() else if (roads[0].mStatus > Road.STATUS_TECHNICAL_ISSUE) //functional issues
//            Toast.makeText(map.context, "No possible route here", Toast.LENGTH_SHORT).show()
//        mRoadOverlays = arrayOfNulls(roads.size)
//        for (i in roads.indices) {
//            val roadPolyline = RoadManager.buildRoadOverlay(roads[i])
//            mRoadOverlays!![i] = roadPolyline
//            val routeDesc = roads[i].getLengthDurationText(this, -1)
//            roadPolyline.title = getString(R.string.route) + " - " + routeDesc
//            roadPolyline.infoWindow = BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map)
//            roadPolyline.relatedObject = i
//            roadPolyline.setOnClickListener(RoadOnClickListener())
//            mapOverlays.add(1, roadPolyline)
//            //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
//            //to avoid covering the other overlays.
//        }
//        selectRoad(0)
//    }
//
//    /**
//     * Async task to get the road in a separate thread.
//     */
//
//    private fun updateRoadTask(vararg params: ArrayList<GeoPoint>){
//        var road: Array<Road>
//        CoroutineScope(Dispatchers.Main).launch{
//            withContext(Dispatchers.IO) {
//                val waypoints = params[0]
//                val roadManager: RoadManager
//                roadManager = OSRMRoadManager(this@MapsActivity, userAgent)
//                road = roadManager.getRoads(waypoints)
//            }
//            withContext(Dispatchers.Main){
//                mRoads = road
//                updateUIWithRoads(mRoads)
//            }
//        }
//    }
////    private inner class UpdateRoadTask(private val mContext: Context) :
////        AsyncTask<ArrayList<GeoPoint?>?, Void?, Array<Road>?>() {
////        private override fun doInBackground(vararg params: ArrayList<GeoPoint?>): Array<Road>? {
////            val waypoints = params[0]
////            val roadManager: RoadManager
////            val locale = Locale.getDefault()
////            when (mWhichRouteProvider) {
////                OSRM -> roadManager = OSRMRoadManager(mContext, userAgent)
////                GRAPHHOPPER_FASTEST -> {
////                    roadManager = GraphHopperRoadManager(graphHopperApiKey, false)
////                    roadManager.addRequestOption("locale=" + locale.language)
////                }
////                GRAPHHOPPER_BICYCLE -> {
////                    roadManager = GraphHopperRoadManager(graphHopperApiKey, false)
////                    roadManager.addRequestOption("locale=" + locale.language)
////                    roadManager.addRequestOption("vehicle=bike")
////                }
////                GRAPHHOPPER_PEDESTRIAN -> {
////                    roadManager = GraphHopperRoadManager(graphHopperApiKey, false)
////                    roadManager.addRequestOption("locale=" + locale.language)
////                    roadManager.addRequestOption("vehicle=foot")
////                }
////                GOOGLE_FASTEST -> roadManager = GoogleRoadManager()
////                else -> return null
////            }
////            return roadManager.getRoads(waypoints)
////        }
////
////        override fun onPostExecute(result: Array<Road>?) {
////            mRoads = result
////            updateUIWithRoads(result)
////            getPOIAsync(poiTagText.text.toString())
////        }
////    }
//
//    //use my current location as itinerary start point:
//    //add intermediate via points:
//    val roadAsync: Unit
//        get() {
//            mRoads = null
//            var roadStartPoint: GeoPoint? = null
//            if (startPoint != null) {
//                roadStartPoint = startPoint
//            } else if (myLocationOverlay.isEnabled && myLocationOverlay.location != null) {
//                //use my current location as itinerary start point:
//                roadStartPoint = myLocationOverlay.location
//            }
//            if (roadStartPoint == null || destinationPoint == null) {
//                updateUIWithRoads(mRoads)
//                return
//            }
//            val waypoints = ArrayList<GeoPoint>(2)
//            waypoints.add(roadStartPoint)
//            waypoints.add(destinationPoint!!)
//
////            UpdateRoadTask(this).execute(waypoints)
//            updateRoadTask(waypoints)
//        }
//
//    //------------ MapEventsReceiver implementation
//    private var mClickedGeoPoint //any other way to pass the position to the menu ???
//            : GeoPoint? = null
//
//    override fun longPressHelper(p: GeoPoint): Boolean {
//        mClickedGeoPoint = p
//        val searchButton = findViewById<View>(R.id.buttonSearchDest) as Button
//        openContextMenu(searchButton)
//        //menu is hooked on the "Search Destination" button, as it must be hooked somewhere.
//        return true
//    }
//
//    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
//        InfoWindow.closeAllInfoWindowsOn(map)
//        return true
//    }
//
//    //----------- Context Menu when clicking on the map
//    override fun onCreateContextMenu(
//        menu: ContextMenu, v: View,
//        menuInfo: ContextMenuInfo
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        val inflater = menuInflater
//        inflater.inflate(R.menu.map_menu, menu)
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_departure -> {
//                startPoint = GeoPoint(mClickedGeoPoint)
//                markerStart = updateItineraryMarker(
//                    markerStart, startPoint, START_INDEX,
//                    R.string.departure, R.drawable.vehicle_car, -1, null
//                )
//                roadAsync
//                true
//            }
//            R.id.menu_destination -> {
//                destinationPoint = GeoPoint(mClickedGeoPoint)
//                markerDestination = updateItineraryMarker(
//                    markerDestination, destinationPoint, DEST_INDEX,
//                    R.string.destination, R.drawable.marker_ic_gudang_location, -1, null
//                )
//                roadAsync
//                true
//            }
//            else -> super.onContextItemSelected(item)
//        }
//    }
//
//    //------------ LocationListener implementation
//    private val mIgnorer = NetworkLocationIgnorer()
//    var mLastTime: Long = 0 // milliseconds
//    var mSpeed = 0.0 // km/h
//    override fun onLocationChanged(pLoc: Location) {
//        val currentTime = System.currentTimeMillis()
//        if (mIgnorer.shouldIgnore(pLoc.provider, currentTime)) return
//        val dT = (currentTime - mLastTime).toDouble()
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
//        val prevLocation = myLocationOverlay.location
//        myLocationOverlay.location = newLocation
//        myLocationOverlay.setAccuracy(pLoc.accuracy.toInt())
//        if (prevLocation != null && pLoc.provider == LocationManager.GPS_PROVIDER) {
//            mSpeed = pLoc.speed * 3.6
//            val speedInt = Math.round(mSpeed)
//            val speedTxt = findViewById<View>(R.id.speed) as TextView
//            speedTxt.text = "$speedInt km/h"
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
//    }
//
//    fun createTrack(id: String, name: String?): KmlTrack {
//        val t = KmlTrack()
//        val p = KmlPlacemark()
//        p.mId = id
//        p.mName = name
//        p.mGeometry = t
//        //set a color to this track by creating a style:
//        val s = Style()
//        var color: Int
//        try {
//            color = id.toInt()
//            color %= TrackColor.size
//            color = TrackColor[color]
//        } catch (e: NumberFormatException) {
//            color = Color.GREEN - 0x20000000
//        }
//        s.mLineStyle = LineStyle(color, 8.0f)
//        p.mStyle = s.toString()
//        return t
//    }
//
//    override fun onProviderDisabled(provider: String) {}
//    override fun onProviderEnabled(provider: String) {}
//    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//
//    //------------ SensorEventListener implementation
//    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
//        myLocationOverlay.setAccuracy(accuracy)
//        map.invalidate()
//    }
//
//    //static float mAzimuthOrientation = 0.0f;
//    override fun onSensorChanged(event: SensorEvent) {
//        when (event.sensor.type) {
//            Sensor.TYPE_ORIENTATION -> if (mSpeed < 0.1) {
//                /* TODO Filter to implement...
//					float azimuth = event.values[0];
//					if (Math.abs(azimuth-mAzimuthOrientation)>2.0f){
//						mAzimuthOrientation = azimuth;
//						myLocationOverlay.setBearing(mAzimuthOrientation);
//						if (mTrackingMode)
//							map.setMapOrientation(-mAzimuthOrientation);
//						else
//							map.invalidate();
//					}
//					*/
//            }
//            else -> {}
//        }
//    }
//
//    companion object {
//
//        private var START_INDEX = -2
//        private var DEST_INDEX = -1
//        var mRoads : Array<Road>? = null
//        private const val ROUTE_REQUEST = 1
//        const val OSRM = 0
//        var SHARED_PREFS_APPKEY = "OSMNavigator"
//        var PREF_LOCATIONS_KEY = "PREF_LOCATIONS"
//        const val userAgent = BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME
//
//        var TrackColor = intArrayOf(
//            Color.CYAN - 0x20000000,
//            Color.BLUE - 0x20000000,
//            Color.MAGENTA - 0x20000000,
//            Color.RED - 0x20000000,
//            Color.YELLOW - 0x20000000
//        )
//    }
//}