package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.databinding.FragmentBarangPeminjamanSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanBarangTidakHabisPakaiItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BarangPeminjamanSuratJalanFragment : DialogFragment() {
    private var _binding: FragmentBarangPeminjamanSuratJalanBinding? = null
    private val binding get() = _binding!!
    private val barangViewModel: BarangPeminjamanSuratJalanViewModel by activityViewModels()
    private var barang: PeminjamanBarangTidakHabisPakaiItem? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarangPeminjamanSuratJalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (barang != null) {
            barangViewModel.setBarang(barang)
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            barangViewModel.barang.observe(viewLifecycleOwner) { barang ->
                tvNama.text = barang?.nama
                tvMerk.text = barang?.merk
                ivImage.setImageFromUrl(barang?.gambar, requireContext())
                btnQrCode.setOnClickListener {
                    CustomDialog(
                        title = "Qr Code ${barang?.nama} (${barang?.merk})",
                        imageUrl = "https://api.qrserver.com/v1/create-qr-code/?size=550x550&data=${barang?.id}"
                    ).show(requireActivity().supportFragmentManager, "QR_CODE")
                }
                tvNomorSeri.text = getString(R.string.nomor_seri_format, barang?.nomorSeri)
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
        val TAG: String = BarangPeminjamanSuratJalanFragment::class.java.simpleName
        fun newInstance(barang: PeminjamanBarangTidakHabisPakaiItem): BarangPeminjamanSuratJalanFragment {
            val fragment = BarangPeminjamanSuratJalanFragment()
            val bundle = Bundle()
            fragment.barang = barang
            fragment.arguments = bundle
            return fragment
        }
    }

}