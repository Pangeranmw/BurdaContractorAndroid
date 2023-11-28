package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.databinding.FragmentBarangPenggunaanSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanBarangHabisPakaiItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BarangPenggunaanSuratJalanFragment : DialogFragment() {
    private var _binding: FragmentBarangPenggunaanSuratJalanBinding? = null
    private val binding get() = _binding!!
    private var barang: PenggunaanBarangHabisPakaiItem? = null
    private val barangViewModel: BarangPenggunaanSuratJalanViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarangPenggunaanSuratJalanBinding.inflate(inflater, container, false)
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
                tvUkuran.text = barang?.ukuran
                ivImage.setImageFromUrl(barang?.gambar, requireContext())
                tvJumlahSatuan.text = barang?.jumlahSatuan
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
        val TAG: String = BarangPenggunaanSuratJalanFragment::class.java.simpleName
        fun newInstance(barang: PenggunaanBarangHabisPakaiItem): BarangPenggunaanSuratJalanFragment {
            val fragment = BarangPenggunaanSuratJalanFragment()
            val bundle = Bundle()
            fragment.barang = barang
            fragment.arguments = bundle
            return fragment
        }
    }

}