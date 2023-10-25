package com.android.burdacontractor.feature.deliveryorder.presentation.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setToastShort
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentAddDataDeliveryOrderBinding
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangFragment
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticFragment
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticViewModel
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDataDeliveryOrderFragment : Fragment() {
    private var _binding: FragmentAddDataDeliveryOrderBinding? = null
    private val addDeliveryOrderViewModel: AddDeliveryOrderViewModel by activityViewModels()
    private val pilihPerusahaanViewModel: PilihPerusahaanViewModel by activityViewModels()
    private val pilihGudangViewModel: PilihGudangViewModel by activityViewModels()
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by activityViewModels()
    private val pilihLogisticViewModel: PilihLogisticViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDataDeliveryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {

        pilihPerusahaanViewModel.perusahaan.observe(viewLifecycleOwner) { perusahaan ->
            binding.cvGudang.setOnClickListener {
                if (perusahaan == null) {
                    requireActivity().setToastShort("Harap Isi Data Secara Berurutan")
                } else {
                    val pilihGudangFragment = PilihGudangFragment.newInstance()
                    pilihGudangFragment.show(requireActivity().supportFragmentManager)
                }
            }
            pilihGudangViewModel.gudang.observe(viewLifecycleOwner) { gudang ->
                gudang?.let {
                    binding.tvGudangBelumDipilih.setGone()
                    binding.layoutGudang.setVisible()
                    binding.ivGudang.setImageFromUrl(gudang.gambar, requireContext())
                    binding.tvAlamatGudang.text = gudang.alamat
                }
                binding.cvLogistic.setOnClickListener {
                    if (gudang == null || perusahaan == null) {
                        requireActivity().setToastShort("Harap Isi Data Secara Berurutan")
                    } else {
                        val pilihLogisticFragment = PilihLogisticFragment.newInstance()
                        pilihLogisticFragment.show(requireActivity().supportFragmentManager)
                    }
                }
                pilihLogisticViewModel.logistic.observe(viewLifecycleOwner) { logistic ->
                    binding.cvLogistic.setOnClickListener {

                    }
                    logistic?.let { lgtc ->
                        if (lgtc.kendaraan == null) {
                            binding.cvKendaraan.setOnClickListener {
                                // tampilkan dialog pilih kendaraan
//                        val pilihKendaraanFragment = PilihKendaraanFragment.newInstance()
//                        pilihKendaraanFragment.show(requireActivity().supportFragmentManager)
                            }
                            pilihKendaraanViewModel.kendaraan.observe(viewLifecycleOwner) { kendaraan ->
                                kendaraan?.let { kdrn ->
                                    showKendaraan(kdrn)
                                } ?: hideKendaraan()
                            }
                        } else hideKendaraan()

                        binding.ivLogistic.setImageFromUrl(logistic.foto, requireContext())
                        binding.tvNamaLogistic.text = logistic.nama
                    }
                }
            }
        }

    }

    private fun showKendaraan(kendaraan: AllKendaraan) {
        binding.tvKendaraanBelumDipilih.setGone()
        binding.layoutKendaraan.setVisible()
        binding.ivKendaraan.setImageFromUrl(kendaraan.gambar, requireContext())
        binding.tvNamaKendaraan.text = kendaraan.merk
        binding.tvPlatKendaraan.text = kendaraan.platNomor
    }

    private fun hideKendaraan() {
        binding.tvKendaraanBelumDipilih.setVisible()
        binding.layoutKendaraan.setGone()
    }

    private fun showPerusahaan(perusahaan: AllPerusahaan) {
        binding.tvPerusahaanBelumDipilih.setGone()
        binding.layoutPerusahaan.setVisible()
        binding.ivPerusahaan.setImageFromUrl(perusahaan.gambar, requireContext())
        binding.tvNamaPerusahaan.text = perusahaan.nama
        binding.tvAlamatPerusahaan.text = perusahaan.alamat
    }

    private fun hidePerusahaan() {
        binding.tvPerusahaanBelumDipilih.setVisible()
        binding.layoutPerusahaan.setGone()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}