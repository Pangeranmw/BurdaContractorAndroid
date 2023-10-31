package com.android.burdacontractor.feature.deliveryorder.presentation.update

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.setEditableText
import com.android.burdacontractor.databinding.FragmentAddPreOrderBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePreOrderFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddPreOrderBinding? = null
    private var preOrderId: String? = null
    private var listener: () -> Unit = {}
    private val updateDeliveryOrderViewModel: UpdateDeliveryOrderViewModel by activityViewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPreOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bsd = dialog as BottomSheetDialog
            val bottomSheet =
                bsd.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.semi_rounded_top_white)
            // FullScreen
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
        }
        return bottomSheetDialog
    }

    private fun initUi() {
        with(updateDeliveryOrderViewModel) {
            with(binding) {
                etNamaMaterial.doAfterTextChanged {
                    requireContext().emptyData(it.toString(), etNamaMaterialLayout)
                    isInputCorrect()
                }
                etUkuran.doAfterTextChanged {
                    requireContext().emptyData(it.toString(), etUkuranLayout)
                    isInputCorrect()
                }
                etSatuan.doAfterTextChanged {
                    requireContext().emptyData(it.toString(), etSatuanLayout)
                    isInputCorrect()
                }
                etJumlah.doAfterTextChanged {
                    requireContext().emptyData(it.toString(), etJumlahLayout)
                    isInputCorrect()
                }
                if (preOrderId != null) {
                    val selectedPo =
                        updateDeliveryOrderViewModel.listPreOrder.value!!.find { it.id == preOrderId } as PreOrder
                    etNamaMaterial.setEditableText(selectedPo.namaMaterial)
                    etJumlah.setEditableText(selectedPo.jumlah.toString())
                    etUkuran.setEditableText(selectedPo.ukuran)
                    etSatuan.setEditableText(selectedPo.satuan)
                    selectedPo.keterangan?.let { etKeterangan.setEditableText(it) }

                    btnSubmit.text = getString(R.string.ubah_pre_order)
                    btnSubmit.setOnClickListener {
                        changePreOrder(
                            id = preOrderId.toString(),
                            ukuran = etUkuran.text.toString(),
                            keterangan = etKeterangan.text.toString(),
                            jumlah = etJumlah.text.toString().toInt(),
                            namaMaterial = etNamaMaterial.text.toString(),
                            satuan = etSatuan.text.toString()
                        )
                        dismiss()
                        listener()
                    }
                } else {
                    btnSubmit.setOnClickListener {
                        addPreOrder(
                            ukuran = etUkuran.text.toString(),
                            keterangan = etKeterangan.text.toString(),
                            jumlah = etJumlah.text.toString().toInt(),
                            namaMaterial = etNamaMaterial.text.toString(),
                            satuan = etSatuan.text.toString()
                        )
                        dismiss()
                        listener()
                    }
                }
            }
        }
    }

    private fun isInputCorrect() {
        with(binding) {
            btnSubmit.isEnabled = TextUtils.isEmpty(etNamaMaterialLayout.error)
                    && TextUtils.isEmpty(etUkuranLayout.error)
                    && TextUtils.isEmpty(etSatuanLayout.error)
                    && TextUtils.isEmpty(etJumlahLayout.error)
                    && etNamaMaterial.text.toString().isNotEmpty()
                    && etUkuran.text.toString().isNotEmpty()
                    && etSatuan.text.toString().isNotEmpty()
                    && etJumlah.text.toString().isNotEmpty()
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
        val TAG = UpdatePreOrderFragment::class.java.simpleName
        fun newInstance(preOrderId: String? = null, listener: () -> Unit): UpdatePreOrderFragment {
            val fragment = UpdatePreOrderFragment()
            if (preOrderId != null) {
                fragment.preOrderId = preOrderId
            }
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}