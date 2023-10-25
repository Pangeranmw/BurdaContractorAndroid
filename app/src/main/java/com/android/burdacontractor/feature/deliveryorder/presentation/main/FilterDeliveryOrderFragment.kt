package com.android.burdacontractor.feature.deliveryorder.presentation.main

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.withDateFormat
import com.android.burdacontractor.databinding.FragmentFilterDeliveryOrderListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker

class FilterDeliveryOrderFragment : BottomSheetDialogFragment() {
    private var onClickListener: OnClickListener? = null
    private val deliveryOrderViewModel: DeliveryOrderViewModel by activityViewModels()

    interface OnClickListener {
        fun onClickListener()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterDeliveryOrderListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDeliveryOrderListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bsd = dialog as BottomSheetDialog
            val bottomSheet =
                bsd.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.semi_rounded_top_white)
            // FullScreen
//            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            val layoutParams = bottomSheet.layoutParams
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//            bottomSheet.layoutParams = layoutParams
        }
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deliveryOrderViewModel.createdByOrFor.observe(viewLifecycleOwner) { createdByOrFor ->
            setCurrentButtonCreatedByOrFor(createdByOrFor)
        }
        setCurrentDateStartAndEnd(
            deliveryOrderViewModel.dateStart.value,
            deliveryOrderViewModel.dateEnd.value
        )
        binding.btnChooseDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText("Pilih Rentang Tanggal")
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val currentDateStart = getDateFromMillis(selection.first, "yyyy/MM/dd")
                val currentDateEnd = getDateFromMillis(selection.second, "yyyy/MM/dd")
                setCurrentDateStartAndEnd(currentDateStart, currentDateEnd)
            }
            datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        }
        binding.btnForAll.setOnClickListener {
            deliveryOrderViewModel.setCreatedByOrFor(CreatedByOrFor.all)
            setCurrentButtonCreatedByOrFor(deliveryOrderViewModel.createdByOrFor.value!!)
        }
        binding.btnForSelf.setOnClickListener {
            deliveryOrderViewModel.setCreatedByOrFor(CreatedByOrFor.self)
            setCurrentButtonCreatedByOrFor(deliveryOrderViewModel.createdByOrFor.value!!)
        }
        binding.btnResetDate.setOnClickListener {
            deliveryOrderViewModel.setDate(null, null)
            binding.tvRangeDate.text = getString(R.string.tanggal_belum_diatur)
            binding.btnResetDate.setGone()
        }
        binding.btnAtur.setOnClickListener {
            onClickListener?.onClickListener()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
    private fun setButtonStyle(buttonSelected: AppCompatButton, buttonUnselected: AppCompatButton){
        buttonSelected.background = AppCompatResources.getDrawable(requireContext(), R.drawable.semi_rounded_secondary_main)
        buttonSelected.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.white))
        buttonSelected.isEnabled = false
        buttonUnselected.background = AppCompatResources.getDrawable(requireContext(), R.drawable.semi_rounded_outline_secondary_main)
        buttonUnselected.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.secondary_main))
        buttonUnselected.isEnabled = true
    }
    private fun setCurrentButtonCreatedByOrFor(createdByOrFor: CreatedByOrFor){
        when(createdByOrFor){
            CreatedByOrFor.all -> {
                setButtonStyle(binding.btnForAll, binding.btnForSelf)
            }
            CreatedByOrFor.self -> {
                setButtonStyle(binding.btnForSelf, binding.btnForAll)
            }
        }
    }
    private fun setCurrentDateStartAndEnd(dateStart: String?, dateEnd: String?){
        dateStart?.let{start->
            dateEnd?.let{end->
                binding.btnResetDate.setVisible()
                deliveryOrderViewModel.setDate(start, end)
                val formattedStartDate = start.withDateFormat()
                val formattedEndDate = end.withDateFormat()
                binding.tvRangeDate.text =
                    getString(R.string.range_date, formattedStartDate, formattedEndDate)
            }
        } ?: binding.btnResetDate.setGone()
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG = FilterDeliveryOrderFragment::class.java.simpleName
        fun newInstance(): FilterDeliveryOrderFragment {
            val fragment = FilterDeliveryOrderFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}