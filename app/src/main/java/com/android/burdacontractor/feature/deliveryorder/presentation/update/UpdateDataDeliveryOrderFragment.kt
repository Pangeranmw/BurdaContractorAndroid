package com.android.burdacontractor.feature.deliveryorder.presentation.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setEditableText
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setToastShort
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.withDateFormat
import com.android.burdacontractor.databinding.FragmentAddDataDeliveryOrderBinding
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangFragment
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanFragment
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanFragment
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanViewModel
import com.android.burdacontractor.feature.profile.presentation.SignatureActivity
import com.android.burdacontractor.feature.proyek.presentation.PilihLogisticFragment
import com.android.burdacontractor.feature.proyek.presentation.PilihLogisticViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateDataDeliveryOrderFragment : Fragment() {
    private var _binding: FragmentAddDataDeliveryOrderBinding? = null
    private val updateDeliveryOrderViewModel: UpdateDeliveryOrderViewModel by activityViewModels()
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
        updateDeliveryOrderViewModel.perihal.value?.let {
            binding.etPerihal.setEditableText(it)
        }
        updateDeliveryOrderViewModel.untukPerhatian.value?.let {
            binding.etUntukPerhatian.setEditableText(it)
        }
        binding.cvTglPengambilan.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Pilih Tanggal Pengambilan")
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val tglPengambilan = getDateFromMillis(selection, "MM/dd/yyyy")
                val tglPengambilanFormatted = getDateFromMillis(selection, "dd MMMM yyyy")
                updateDeliveryOrderViewModel.setTglPengambilan(tglPengambilan)
                binding.tvTglPengambilan.text = tglPengambilanFormatted
            }
            datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
            clearEditTextFocus()
        }
        binding.etPerihal.setOnFocusChangeListener { view, b ->
            val text = binding.etPerihal.text.toString()
            requireContext().emptyData(text, binding.etPerihalLayout)
            if (!b) {
                updateDeliveryOrderViewModel.setPerihal(text)
            }
        }
        binding.etUntukPerhatian.setOnFocusChangeListener { view, b ->
            val text = binding.etUntukPerhatian.text.toString()
            requireContext().emptyData(text, binding.etUntukPerhatianLayout)
            if (!b) {
                updateDeliveryOrderViewModel.setUntukPerhatian(text)
            }
        }
        binding.btnUbahTtd.setOnClickListener {
            requireActivity().openActivity(SignatureActivity::class.java, false)
            clearEditTextFocus()
        }
        binding.ivTtd.setImageFromUrl(storageViewModel.ttd, requireContext())
        updateDeliveryOrderViewModel.tglPengambilan.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvTglPengambilan.text = it.withDateFormat("MM/dd/yyyy", "dd MMMM yyyy")
            }
        }
        pilihPerusahaanViewModel.perusahaan.observe(viewLifecycleOwner) { perusahaan ->
            binding.cvPerusahaan.setOnClickListener {
                val pilihPerusahaanFragment = PilihPerusahaanFragment.newInstance()
                pilihPerusahaanFragment.show(requireActivity().supportFragmentManager)
                clearEditTextFocus()
            }
            perusahaan?.let {
                binding.tvPerusahaanBelumDipilih.setGone()
                binding.layoutPerusahaan.setVisible()
                binding.ivPerusahaan.setImageFromUrl(perusahaan.gambar, requireContext())
                binding.tvAlamatPerusahaan.text = perusahaan.alamat
                binding.tvNamaPerusahaan.text = perusahaan.nama
            }
            binding.cvGudang.setOnClickListener {
                if (perusahaan == null) {
                    requireActivity().setToastShort("Harap Isi Data Secara Berurutan")
                } else {
                    val pilihGudangFragment = PilihGudangFragment.newInstance()
                    pilihGudangFragment.show(requireActivity().supportFragmentManager)
                }
                clearEditTextFocus()
            }
            pilihGudangViewModel.gudang.observe(viewLifecycleOwner) { gudang ->
                gudang?.let {
                    binding.tvGudangBelumDipilih.setGone()
                    binding.layoutGudang.setVisible()
                    binding.ivGudang.setImageFromUrl(gudang.gambar, requireContext())
                    binding.tvNamaGudang.text = gudang.nama
                    binding.tvAlamatGudang.text = gudang.alamat
                }
                binding.cvLogistic.setOnClickListener {
                    if (gudang == null || perusahaan == null) {
                        requireActivity().setToastShort("Harap Isi Data Secara Berurutan")
                    } else {
                        val pilihLogisticFragment = PilihLogisticFragment.newInstance()
                        pilihLogisticFragment.show(requireActivity().supportFragmentManager)
                    }
                    clearEditTextFocus()
                }
                pilihLogisticViewModel.logistic.observe(viewLifecycleOwner) { logistic ->
                    logistic?.let { lgtc ->
                        binding.tvLogisticBelumDipilih.setGone()
                        binding.layoutLogistic.setVisible()
                        binding.ivLogistic.setImageFromUrl(lgtc.foto, requireContext())
                        binding.tvNamaLogistic.text = lgtc.nama
                    }
                    if (logistic?.kendaraan == null) {
                        pilihKendaraanViewModel.setKendaraan(null)
                    } else {
                        val vehicle = AllKendaraan(
                            merk = logistic.kendaraan.merk,
                            totalData = 1,
                            createdAt = logistic.kendaraan.createdAt,
                            gambar = logistic.kendaraan.gambar,
                            namaLogistic = logistic.nama,
                            gudangId = logistic.kendaraan.gudangId,
                            updatedAt = logistic.kendaraan.updatedAt,
                            logisticId = logistic.kendaraan.logisticId,
                            jenis = logistic.kendaraan.jenis,
                            id = logistic.kendaraan.id,
                            platNomor = logistic.kendaraan.platNomor,
                            namaGudang = gudang?.nama ?: "",
                            status = logistic.kendaraan.status
                        )
                        pilihKendaraanViewModel.setKendaraan(vehicle)
                    }
                    binding.cvKendaraan.setOnClickListener {
                        if (gudang == null || perusahaan == null || logistic == null) {
                            requireActivity().setToastShort("Harap Isi Data Secara Berurutan")
                        } else {
                            if (logistic.kendaraan == null) {
                                val pilihKendaraanFragment = PilihKendaraanFragment.newInstance()
                                pilihKendaraanFragment.show(requireActivity().supportFragmentManager)
                            }
                        }
                        clearEditTextFocus()
                    }
                    pilihKendaraanViewModel.kendaraan.observe(viewLifecycleOwner) { kendaraan ->
                        kendaraan?.let { kdrn ->
                            showKendaraan(kdrn)
                        } ?: hideKendaraan()
                    }
                }
            }
        }
    }

    private fun clearEditTextFocus() {
        if (binding.etPerihal.isFocused) binding.etPerihal.clearFocus()
        if (binding.etUntukPerhatian.isFocused) binding.etUntukPerhatian.clearFocus()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}