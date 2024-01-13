package com.android.burdacontractor.core.presentation.pinpoint

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.PinPoint
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFindPlaceAdapter
import com.android.burdacontractor.core.presentation.customview.MarkerInfoWindowAdapter
import com.android.burdacontractor.core.utils.BitmapHelper
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.databinding.FragmentPinPointLokasiBinding
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
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
class PinPointLokasiFragment : DialogFragment(), OnMapReadyCallback {
    private var _binding: FragmentPinPointLokasiBinding? = null
    private val binding get() = _binding!!
    private val pinPointLokasiViewModel: PinPointLokasiViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private var mapView: MapView? = null
    private var marker: Marker? = null
    private val markerIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.primary_main)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.marker_ic_marker, color)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinPointLokasiBinding.inflate(inflater, container, false)
        mapView = binding.pinPointMap
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        marker = mMap.addMarker(
            MarkerOptions()
                .title("Lokasi Pin Point")
                .position(mMap.cameraPosition.target)
                .icon(markerIcon)
        )
        if (pinPointLokasiViewModel.longitude.value != null && pinPointLokasiViewModel.latitude.value != null) {
            val selectedCoordinate = LatLng(
                pinPointLokasiViewModel.latitude.value!!.toDouble(),
                pinPointLokasiViewModel.longitude.value!!.toDouble()
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedCoordinate, 20f))
        } else {
            trackCurrentLocation {
                val burdaCoordinate = LatLng(-6.2501422, 106.8564921)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(burdaCoordinate, 20f))
            }
        }
        mMap.setOnCameraMoveListener {
            var mustShown = false
            if (marker!!.isInfoWindowShown) {
                marker?.hideInfoWindow()
                mustShown = true
            }
            marker?.position = mMap.cameraPosition.target
            marker?.tag = PinPoint("Lokasi Pin Point", mMap.cameraPosition.target.toString())
            if (mustShown) {
                marker?.showInfoWindow()
            }
            binding.etLatitude.setText(mMap.cameraPosition.target.latitude.toString())
            binding.etLongitude.setText(mMap.cameraPosition.target.longitude.toString())
        }
        initUi()
    }

    private fun trackCurrentLocation(listener: () -> Unit) {
        if (storageViewModel.getTracking) {
            val currentLocation =
                LatLng(storageViewModel.latitude.toDouble(), storageViewModel.longitude.toDouble())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20f))
        } else {
            showSnackbarToProfile()
            listener()
        }
    }

    private fun showSnackbarToProfile() {
        val snackbar = Snackbar.make(
            binding.pinPointMap,
            "Harap aktifkan tracking pada menu profil",
            Snackbar.ANIMATION_MODE_SLIDE
        )
        snackbar.setAction("Profil") {
            requireActivity().openActivity(ProfileActivity::class.java)
        }
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun isInputCorrect() {
        binding.apply {
            btnSubmit.isEnabled = TextUtils.isEmpty(etLatitudeLayout.error)
                    && TextUtils.isEmpty(etLongitudeLayout.error)
                    && etLatitude.text.toString().isNotEmpty()
                    && etLongitude.text.toString().isNotEmpty()
        }
    }

    private fun initUi() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    pinPointLokasiViewModel.getPlaceByQuery(searchView.text.toString()) { list ->
                        val adapter = ListFindPlaceAdapter {
                            val selectedLat = it.location.latitude.toString()
                            val selectedLon = it.location.longitude.toString()
                            val coordinate = LatLng(selectedLat.toDouble(), selectedLon.toDouble())
                            searchView.hide()
                            pinPointLokasiViewModel.setAlamat(it.formattedAddress)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20f))
                        }
                        adapter.submitList(list)
                        rvSearch.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        rvSearch.adapter = adapter
                    }
                    false
                }
            etLongitude.doAfterTextChanged {
                requireActivity().emptyData(it.toString(), etLongitudeLayout)
                isInputCorrect()
            }
            etLatitude.doAfterTextChanged {
                requireActivity().emptyData(it.toString(), etLatitudeLayout)
                isInputCorrect()
            }
            btnCurrentLocation.setOnClickListener {
                trackCurrentLocation {}
            }
            btnBack.setOnClickListener {
                dismiss()
            }
            btnSubmit.setOnClickListener {
                pinPointLokasiViewModel.setLatitude(etLatitude.text.toString())
                pinPointLokasiViewModel.setLongitude(etLongitude.text.toString())
                if (pinPointLokasiViewModel.alamat.value == null) {
                    pinPointLokasiViewModel.getLocationFromCoordinate { dismiss() }
                } else {
                    dismiss()
                }
            }
            tvPerbaruiMarker.setOnClickListener {
                val coordinate = LatLng(
                    etLatitude.text.toString().toDouble(),
                    etLongitude.text.toString().toDouble()
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20f))
            }
            cbInputManual.setOnCheckedChangeListener { _, b ->
                etLatitude.isEnabled = b
                etLongitude.isEnabled = b
                tvPerbaruiMarker.isVisible = b
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {
        val TAG: String = PinPointLokasiFragment::class.java.simpleName
        fun newInstance(): PinPointLokasiFragment {
            val fragment = PinPointLokasiFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}