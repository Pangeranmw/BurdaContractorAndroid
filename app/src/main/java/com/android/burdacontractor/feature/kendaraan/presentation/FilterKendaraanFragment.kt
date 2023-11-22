package com.android.burdacontractor.feature.kendaraan.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentFilterKendaraanDialogBinding
import com.android.burdacontractor.feature.kendaraan.presentation.main.KendaraanViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView


class FilterKendaraanFragment : BottomSheetDialogFragment() {
    private val kendaraanViewModel: KendaraanViewModel by activityViewModels()
    private var onClickListener: OnClickListener? = null
    private var statusIndex: Int? = null
    private var jenisIndex: Int? = null
    private var gudangIndex: Int? = null
    private var isChange: Boolean = false

    interface OnClickListener {
        fun onClickListener()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterKendaraanDialogBinding? = null
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
        _binding = FragmentFilterKendaraanDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            kendaraanViewModel.listGudang.observe(viewLifecycleOwner) { listGudang ->
                val list = listGudang.map { it.nama }
                spinnerGudang.setItems(list)
                kendaraanViewModel.gudangIndex.value.let {
                    gudangIndex = it
                    if (it != null) spinnerGudang.selectItemByIndex(it)
                }
                btnResetGudang.setOnClickListener {
                    gudangIndex = null
                    resetSpinner(spinnerGudang, btnResetGudang, list)
                }
            }
            spinnerGudang.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                gudangIndex = newIndex
                btnAtur.isVisible = isChange()
                btnResetGudang.setVisible()
            })

            kendaraanViewModel.listJenis.observe(viewLifecycleOwner) { listJenis ->
                val list = listJenis.map { enumValueToNormal(it) }
                spinnerJenis.setItems(list)
                kendaraanViewModel.jenisIndex.value.let {
                    jenisIndex = it
                    if (it != null) spinnerJenis.selectItemByIndex(it)
                }
                btnResetJenis.setOnClickListener {
                    jenisIndex = null
                    resetSpinner(spinnerJenis, btnResetJenis, list)
                }
            }
            spinnerJenis.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                jenisIndex = newIndex
                btnAtur.isVisible = isChange()
                btnResetJenis.setVisible()
            })

            kendaraanViewModel.listStatus.observe(viewLifecycleOwner) { listStatus ->
                val list = listStatus.map { enumValueToNormal(it) }
                spinnerStatus.setItems(list)
                kendaraanViewModel.statusIndex.value.let {
                    statusIndex = it
                    if (it != null) spinnerStatus.selectItemByIndex(it)
                }
                btnResetStatus.setOnClickListener {
                    statusIndex = null
                    resetSpinner(spinnerStatus, btnResetStatus, list)
                }
            }
            spinnerStatus.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                statusIndex = newIndex
                btnAtur.isVisible = isChange()
                btnResetStatus.setVisible()
            })

            btnAtur.setOnClickListener {
                if (isChange()) {
                    kendaraanViewModel.setStatusIndex(statusIndex)
                    kendaraanViewModel.setJenisIndex(jenisIndex)
                    kendaraanViewModel.setGudangIndex(gudangIndex)
                    onClickListener?.onClickListener()
                }
                dismiss()
            }
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun isChange(): Boolean {
        return statusIndex != kendaraanViewModel.statusIndex.value
                || gudangIndex != kendaraanViewModel.gudangIndex.value
                || jenisIndex != kendaraanViewModel.jenisIndex.value
    }

    private fun resetSpinner(
        spinner: PowerSpinnerView,
        btnReset: AppCompatButton,
        list: List<String>
    ) {
        spinner.clearSelectedItem()
        spinner.setItems(emptyList<String>())
        spinner.setItems(list)
        binding.btnAtur.isVisible = isChange()
        btnReset.setGone()
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {

        val TAG = FilterKendaraanFragment::class.java.simpleName
        fun newInstance(): FilterKendaraanFragment {
            val fragment = FilterKendaraanFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
//        isChange = false
        Log.d("onDismiss isChange", isChange.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}