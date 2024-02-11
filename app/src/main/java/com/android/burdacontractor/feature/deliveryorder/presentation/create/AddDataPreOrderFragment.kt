package com.android.burdacontractor.feature.deliveryorder.presentation.create

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListPreOrderAdapter
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.withDateFormat
import com.android.burdacontractor.databinding.FragmentAddDataPreOrderBinding
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticViewModel
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDataPreOrderFragment : Fragment() {
    private var _binding: FragmentAddDataPreOrderBinding? = null
    private val addDeliveryOrderViewModel: AddDeliveryOrderViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private val pilihPerusahaanViewModel: PilihPerusahaanViewModel by activityViewModels()
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by activityViewModels()
    private val pilihGudangViewModel: PilihGudangViewModel by activityViewModels()
    private val pilihLogisticViewModel: PilihLogisticViewModel by activityViewModels()
    private lateinit var adapter: ListPreOrderAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDataPreOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        with(binding) {
            adapter = ListPreOrderAdapter(true, { preOrder ->
                val addPOFragment = AddPreOrderFragment.newInstance(preOrderId = preOrder.id) {
                    refreshAdapter()
                }
                addPOFragment.show(requireActivity().supportFragmentManager)
            }, { preOrder ->
                addDeliveryOrderViewModel.removePreOrder(preOrder)
                refreshAdapter()
            })
            dataDeliveryOrder.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            cvDataDeliveryOrder.setOnClickListener {
                val v = if (dataDeliveryOrder.visibility == View.GONE) View.VISIBLE else View.GONE
                ivDown.visibility = dataDeliveryOrder.visibility
                dataDeliveryOrder.visibility = v
            }
            addDeliveryOrderViewModel.listPreOrder.observe(viewLifecycleOwner) {
                refreshAdapter()
            }
            btnSubmit.setOnClickListener {
                with(addDeliveryOrderViewModel) {
                    addCreateStepTwoDo(
                        logisticId = pilihLogisticViewModel.logistic.value!!.id,
                        purchasingId = storageViewModel.userId,
                        perusahaanId = pilihPerusahaanViewModel.perusahaan.value!!.id,
                        gudangId = pilihGudangViewModel.gudang.value!!.id,
                        kendaraanId = pilihKendaraanViewModel.kendaraan.value!!.id,
                        perihal = addDeliveryOrderViewModel.perihal.value!!,
                        tglPengambilan = addDeliveryOrderViewModel.tglPengambilan.value!!,
                        untukPerhatian = addDeliveryOrderViewModel.untukPerhatian.value!!,
                        listPreOrder = addDeliveryOrderViewModel.listPreOrder.value!!
                    ) {
                        requireActivity().openActivityWithExtras(DeliveryOrderDetailActivity::class.java) {
                            putString(INTENT_ID, it)
                        }
                    }
                }
            }
            btnTambahPreOrder.setOnClickListener {
                val addPOFragment = AddPreOrderFragment.newInstance {
                    refreshAdapter()
                }
                addPOFragment.show(requireActivity().supportFragmentManager)
            }
            addDeliveryOrderViewModel.kodeDo.observe(viewLifecycleOwner) { kodeDo ->
                pilihLogisticViewModel.logistic.observe(viewLifecycleOwner) { logistic ->
                    pilihKendaraanViewModel.kendaraan.observe(viewLifecycleOwner) { kendaraan ->
                        pilihPerusahaanViewModel.perusahaan.observe(viewLifecycleOwner) { perusahaan ->
                            pilihGudangViewModel.gudang.observe(viewLifecycleOwner) { gudang ->
                                addDeliveryOrderViewModel.perihal.observe(viewLifecycleOwner) { perihal ->
                                    addDeliveryOrderViewModel.tglPengambilan.observe(
                                        viewLifecycleOwner
                                    ) { tglPengambilan ->
                                        addDeliveryOrderViewModel.untukPerhatian.observe(
                                            viewLifecycleOwner
                                        ) { untukPerhatian ->
                                            if (logistic != null &&
                                                kendaraan != null &&
                                                perusahaan != null &&
                                                gudang != null &&
                                                perihal != null &&
                                                tglPengambilan != null &&
                                                untukPerhatian != null
                                            ) {
                                                tvKodeDo.text = kodeDo
                                                tvTanggalPengambilan.text =
                                                    tglPengambilan.withDateFormat(
                                                        "MM/dd/yyyy",
                                                        "dd MMMM yyyy"
                                                    )
                                                tvPerihal.text = perihal
                                                tvUntukPerhatian.text = untukPerhatian
                                                tvKendaraan.text = getString(
                                                    R.string.merk_plat_kendaraan,
                                                    kendaraan.merk,
                                                    kendaraan.platNomor
                                                )
                                                tvGudang.text = gudang.nama
                                                tvPerusahaan.text = perusahaan.nama
                                                tvLogistic.text = logistic.nama
                                                ivTtd.setImageFromUrl(
                                                    storageViewModel.ttd,
                                                    requireContext()
                                                )
                                                ivGudang.setImageFromUrl(
                                                    gudang.gambar,
                                                    requireContext()
                                                )
                                                ivPerusahaan.setImageFromUrl(
                                                    perusahaan.gambar,
                                                    requireContext()
                                                )
                                                ivKendaraan.setImageFromUrl(
                                                    kendaraan.gambar,
                                                    requireContext()
                                                )
                                                ivLogistic.setImageFromUrl(
                                                    logistic.foto,
                                                    requireContext()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isPoAvailable() {
        val listNotNull = addDeliveryOrderViewModel.listPreOrder.value!!.size > 0
        binding.btnSubmit.isEnabled = listNotNull
    }

    private fun refreshAdapter() {
        binding.rvPreOrder.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvPreOrder.adapter = adapter
        adapter.submitList(null)
        adapter.submitList(addDeliveryOrderViewModel.listPreOrder.value!!)
        isPoAvailable()
        if (addDeliveryOrderViewModel.listPreOrder.value!!.size == 0) binding.tvPoEmpty.setVisible()
        else binding.tvPoEmpty.setGone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}