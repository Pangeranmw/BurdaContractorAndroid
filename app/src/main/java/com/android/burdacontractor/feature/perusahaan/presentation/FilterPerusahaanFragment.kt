package com.android.burdacontractor.feature.perusahaan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentFilterPerusahaanDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener

class FilterPerusahaanFragment(
    private val provinsiIndex: Int?,
    private val listProvinsi: List<String>
) : BottomSheetDialogFragment() {
    private var onClickListener: OnClickListener? = null
    private var currentProvinsiIndex: Int? = null

    interface OnClickListener {
        fun onClickListener(provinsiIndex: Int?)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterPerusahaanDialogBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.RoundedBackgroundBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentProvinsiIndex = provinsiIndex
        _binding = FragmentFilterPerusahaanDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            spinnerProvinsi.setItems(listProvinsi)
            spinnerProvinsi.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                currentProvinsiIndex = newIndex
                btnResetProvinsi.setVisible()
            })
            provinsiIndex?.let {
                spinnerProvinsi.selectItemByIndex(it)
            }
            btnResetProvinsi.setOnClickListener {
                currentProvinsiIndex = null
                spinnerProvinsi.clearSelectedItem()
                spinnerProvinsi.setItems(emptyList<String>())
                spinnerProvinsi.setItems(listProvinsi)
                btnResetProvinsi.setGone()
            }
            btnAtur.setOnClickListener {
                onClickListener?.onClickListener(
                    currentProvinsiIndex
                )
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {

        val TAG = FilterPerusahaanFragment::class.java.simpleName
        fun newInstance(provinsiIndex: Int?, listProvinsi: List<String>): FilterPerusahaanFragment {
            val fragment = FilterPerusahaanFragment(provinsiIndex, listProvinsi)
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