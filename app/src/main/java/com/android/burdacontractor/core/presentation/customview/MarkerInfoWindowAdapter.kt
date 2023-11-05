package com.android.burdacontractor.core.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.PinPoint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(
            R.layout.pin_point_info_contents, null
        )
        val pinPoint = marker.tag as? PinPoint ?: return null

        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = pinPoint.title
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = pinPoint.description
        return view
    }
}