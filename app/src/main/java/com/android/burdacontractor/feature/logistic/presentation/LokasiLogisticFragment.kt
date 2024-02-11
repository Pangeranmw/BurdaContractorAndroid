package com.android.burdacontractor.feature.logistic.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListLokasiLogisticAdapter
import com.android.burdacontractor.core.presentation.customview.MarkerLogisticInfoWindowAdapter
import com.android.burdacontractor.core.utils.BitmapHelper
import com.android.burdacontractor.core.utils.convertDistance
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentLokasiLogisticBinding
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LokasiLogisticFragment : DialogFragment(), OnMapReadyCallback {
    private var _binding: FragmentLokasiLogisticBinding? = null
    private val binding get() = _binding!!
    private val pilihLogisticViewModel: PilihLogisticViewModel by activityViewModels()
    private val lokasiLogisticViewModel: LokasiLogisticViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private var selectedLogistic: AllLogistic? = null
    private var mapView: MapView? = null
    private val driverMarkerIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.primary_main)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.marker_ic_driver_location, color)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLokasiLogisticBinding.inflate(inflater, container, false)
        mapView = binding.pinPointMap
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(MarkerLogisticInfoWindowAdapter(requireContext()))
        with(lokasiLogisticViewModel) {
            state.observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it == StateResponse.LOADING
            }
            listLogistic.observe(viewLifecycleOwner) {
                for (allLogistic in it) {
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    allLogistic.latitude!!,
                                    allLogistic.longitude!!
                                )
                            )
                            .icon(driverMarkerIcon)
                    )
                    marker?.tag = allLogistic
                }
            }
            messageResponse.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (pilihLogisticViewModel.longitude.value != null && pilihLogisticViewModel.latitude.value != null) {
            val selectedDriver = LatLng(
                pilihLogisticViewModel.latitude.value!!.toDouble(),
                pilihLogisticViewModel.longitude.value!!.toDouble()
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedDriver, 20f))
        } else {
            trackCurrentLocation {
                val burdaCoordinate = LatLng(-6.2501422, 106.8564921)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(burdaCoordinate, 20f))
            }
        }
        mMap.setOnMarkerClickListener { marker ->
            val logistic = marker.tag as AllLogistic
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 20f))
            marker.showInfoWindow()
            selectedLogistic = logistic
            binding.cvDriver.setVisible()
            with(binding) {
                tvNama.text = logistic.nama
                logistic.kendaraan?.let {
                    tvKendaraan.setVisible()
                    tvKendaraan.text =
                        requireContext().getString(
                            R.string.merk_plat_kendaraan,
                            it.merk,
                            it.platNomor
                        )
                } ?: tvKendaraan.setGone()

                if (logistic.doActive > 0) {
                    tvDoActive.setVisible()
                    tvDoActive.text =
                        requireContext().getString(R.string.active_do, logistic.doActive)
                } else tvDoActive.setGone()

                if (logistic.sjgpActive > 0) {
                    tvSjgpActive.setVisible()
                    tvSjgpActive.text =
                        requireContext().getString(R.string.active_sjgp, logistic.sjgpActive)
                } else tvSjgpActive.setGone()

                if (logistic.sjppActive > 0) {
                    tvSjppActive.setVisible()
                    tvSjppActive.text =
                        requireContext().getString(R.string.active_sjpp, logistic.sjppActive)
                } else tvSjppActive.setGone()

                if (logistic.sjpgActive > 0) {
                    tvSjpgActive.setVisible()
                    tvSjpgActive.text =
                        requireContext().getString(R.string.active_sjpg, logistic.sjpgActive)
                } else tvSjpgActive.setGone()

                ivLogistic.setImageFromUrl(logistic.foto, requireContext())

                logistic.jarak?.let {
                    tvJarak.setVisible()
                    tvJarak.text = requireContext().getString(
                        R.string.jarak_dari_lokasimu,
                        it.convertDistance()
                    )
                } ?: tvJarak.setGone()
            }
            binding.tvEmptyDriver.setGone()
            isInputCorrect()
            true
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
            btnSubmit.isEnabled = selectedLogistic != null
        }
    }

    private fun initUi() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    val allLogistic = lokasiLogisticViewModel.listLogistic.value!!
                    val findLogistic =
                        allLogistic.filter { it.nama.contains(searchView.text.toString(), true) }
                    val adapter = ListLokasiLogisticAdapter {
                        val selectedLat = it.latitude.toString()
                        val selectedLon = it.longitude.toString()
                        val coordinate = LatLng(selectedLat.toDouble(), selectedLon.toDouble())
                        searchView.hide()
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20f))
                    }
                    adapter.submitList(findLogistic)
                    rvSearch.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    rvSearch.adapter = adapter
                    false
                }
            btnCurrentLocation.setOnClickListener {
                trackCurrentLocation {}
            }
            btnBack.setOnClickListener {
                dismiss()
            }
            btnSubmit.setOnClickListener {
                pilihLogisticViewModel.setLogistic(selectedLogistic!!)
                pilihLogisticViewModel.setLatitude(selectedLogistic!!.latitude.toString())
                pilihLogisticViewModel.setLongitude(selectedLogistic!!.longitude.toString())
                dismiss()
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
        val TAG: String = LokasiLogisticFragment::class.java.simpleName
        fun newInstance(): LokasiLogisticFragment {
            val fragment = LokasiLogisticFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}