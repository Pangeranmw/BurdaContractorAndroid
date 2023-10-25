package com.android.burdacontractor.feature.perusahaan.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentFilterPerusahaanDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener


class FilterPerusahaanFragment : BottomSheetDialogFragment() {
    private val perusahaanViewModel: PerusahaanViewModel by activityViewModels()
    private var onClickListener: OnClickListener? = null
    private var provinsiIndex: Int? = null

    interface OnClickListener {
        fun onClickListener()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterPerusahaanDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bsd = dialog as BottomSheetDialog
            val bottomSheet =
                bsd.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.semi_rounded_top_white)
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterPerusahaanDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            perusahaanViewModel.listProvinsi.observe(viewLifecycleOwner) { listProvinsi ->
                spinnerProvinsi.setItems(listProvinsi)
                perusahaanViewModel.provinsiIndex.value?.let {
                    spinnerProvinsi.selectItemByIndex(it)
                }
                btnResetProvinsi.setOnClickListener {
                    provinsiIndex = null
                    spinnerProvinsi.clearSelectedItem()
                    spinnerProvinsi.setItems(emptyList<String>())
                    spinnerProvinsi.setItems(listProvinsi)
                    btnResetProvinsi.setGone()
                }
            }
            spinnerProvinsi.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                provinsiIndex = newIndex
                btnResetProvinsi.setVisible()
            })
            btnAtur.setOnClickListener {
                perusahaanViewModel.setProvinsiIndex(provinsiIndex)
                onClickListener?.onClickListener()
                dismiss()
            }
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {

        val TAG = FilterPerusahaanFragment::class.java.simpleName
        fun newInstance(): FilterPerusahaanFragment {
            val fragment = FilterPerusahaanFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}