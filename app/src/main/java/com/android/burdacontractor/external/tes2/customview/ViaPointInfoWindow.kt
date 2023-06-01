//package com.android.burdacontractor.presentation.test.tes2.customview
//
//import android.view.View
//import android.widget.ImageButton
//import com.android.burdacontractor.presentation.test.tes2.MapsActivity
//import com.example.osmwithbackgroundlocation2.R
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.Marker
//import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
//
//class ViaPointInfoWindow(layoutResId: Int, mapView: MapView?) : MarkerInfoWindow(layoutResId, mapView) {
//
//    var mSelectedPoint = 0
//
//    init {
//        val btnDelete = mView.findViewById<View>(R.id.bubble_delete) as ImageButton
//        btnDelete.setOnClickListener { view -> //Call the removePoint method on MapActivity.
//            //TODO: find a cleaner way to do that!
//            val mapActivity: MapsActivity = view.context as MapsActivity
//            mapActivity.removePoint(mSelectedPoint)
//            close()
//        }
//    }
//
//    override fun onOpen(item: Any) {
//        val eItem = item as Marker
//        mSelectedPoint = eItem.relatedObject as Int
//        super.onOpen(item)
//    }
//
//    override fun onClose() {
//        super.onClose()
//    }
//
//}