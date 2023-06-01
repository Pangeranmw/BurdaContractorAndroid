//package com.android.burdacontractor.presentation.test.tes2.customview
//
//import android.graphics.drawable.Drawable
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//import com.android.burdacontractor.R
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.infowindow.InfoWindow
//
//class MarkerInfoWindowCustom(private val mapView: MapView,
//                             private val title: String? = null,
//                             private val snippet: String? = null,
//                             private val subDescription: String? = null,
//                             private val icon: Drawable? = null) :
//    InfoWindow(R.layout.custom_info_window_marker, mapView) {
//
//    override fun onOpen(item: Any?) {
//        // Following command
//        closeAllInfoWindowsOn(mapView)
//
//        // Here we are settings onclick listeners for the buttons in the layouts.
//
//        val titleTextView = mView.findViewById<TextView>(R.id.text_view)
//        val snippetTextView = mView.findViewById<TextView>(R.id.tv_snippet)
//        val subDescriptionTextView = mView.findViewById<TextView>(R.id.tv_sub_description)
//        val iconView = mView.findViewById<ImageView>(R.id.iv_info_window)
//
//        setText(title,titleTextView)
//        setText(snippet,snippetTextView)
//        setText(subDescription,subDescriptionTextView)
//        setIcon(icon, iconView)
//        // You can set an onClickListener on the InfoWindow itself.
//        // This is so that you can close the InfoWindow once it has been tapped.
//
//        // Instead, you could also close the InfoWindows when the map is pressed.
//        // This is covered in the Map Listeners guide.
//
//        mView.setOnClickListener {
//            close()
//        }
//    }
//
//    override fun onClose() {
//
//    }
//
//    private fun setText(text: String?, textView: TextView){
//        if(text.isNullOrEmpty()) textView.visibility = View.GONE
//        else textView.text = text
//    }
//
//    private fun setIcon(icon: Drawable?, iv: ImageView){
//        if(icon == null) iv.visibility = View.GONE
//        else iv.setImageDrawable(icon)
//    }
//}