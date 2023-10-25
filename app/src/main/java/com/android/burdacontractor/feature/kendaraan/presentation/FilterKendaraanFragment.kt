package com.android.burdacontractor.feature.kendaraan.presentation

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
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentFilterKendaraanDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener


class FilterKendaraanFragment : BottomSheetDialogFragment() {
    private val kendaraanViewModel: KendaraanViewModel by activityViewModels()
    private var onClickListener: OnClickListener? = null
    private var statusIndex: Int? = null
    private var jenisIndex: Int? = null
    private var gudangIndex: Int? = null

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
//            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            val layoutParams = bottomSheet.layoutParams
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//            bottomSheet.layoutParams = layoutParams
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
                kendaraanViewModel.gudangIndex.value?.let {
                    spinnerGudang.selectItemByIndex(it)
                }
                btnResetGudang.setOnClickListener {
                    gudangIndex = null
                    spinnerGudang.clearSelectedItem()
                    spinnerGudang.setItems(emptyList<String>())
                    spinnerGudang.setItems(list)
                    btnResetGudang.setGone()
                }
            }
            spinnerGudang.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                gudangIndex = newIndex
                btnResetGudang.setVisible()
            })

            kendaraanViewModel.listJenis.observe(viewLifecycleOwner) { listJenis ->
                val list = listJenis.map { enumValueToNormal(it) }
                spinnerJenis.setItems(list)
                kendaraanViewModel.jenisIndex.value?.let {
                    spinnerJenis.selectItemByIndex(it)
                }
                btnResetJenis.setOnClickListener {
                    jenisIndex = null
                    spinnerJenis.clearSelectedItem()
                    spinnerJenis.setItems(emptyList<String>())
                    spinnerJenis.setItems(list)
                    btnResetJenis.setGone()
                }
            }
            spinnerJenis.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                jenisIndex = newIndex
                btnResetJenis.setVisible()
            })

            kendaraanViewModel.listStatus.observe(viewLifecycleOwner) { listStatus ->
                val list = listStatus.map { enumValueToNormal(it) }
                spinnerStatus.setItems(list)
                kendaraanViewModel.statusIndex.value?.let {
                    spinnerStatus.selectItemByIndex(it)
                }
                btnResetStatus.setOnClickListener {
                    statusIndex = null
                    spinnerStatus.clearSelectedItem()
                    spinnerStatus.setItems(emptyList<String>())
                    spinnerStatus.setItems(list)
                    btnResetStatus.setGone()
                }
            }
            spinnerStatus.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                statusIndex = newIndex
                btnResetStatus.setVisible()
            })

            btnAtur.setOnClickListener {
                kendaraanViewModel.setStatusIndex(statusIndex)
                kendaraanViewModel.setJenisIndex(jenisIndex)
                kendaraanViewModel.setGudangIndex(gudangIndex)
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

        val TAG = FilterKendaraanFragment::class.java.simpleName
        fun newInstance(): FilterKendaraanFragment {
            val fragment = FilterKendaraanFragment()
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