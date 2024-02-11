package com.android.burdacontractor.core.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.android.burdacontractor.R
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class MarkerLogisticInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(
            R.layout.pin_point_info_contents, null
        )
        val allLogistic = marker.tag as? AllLogistic ?: return null

        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = allLogistic.nama
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text =
            "${allLogistic.sjgpActive} SJGP, ${allLogistic.sjppActive} SJPP, ${allLogistic.sjpgActive} SJPG, ${allLogistic.doActive} DO"
        return view
    }
}