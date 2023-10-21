package com.android.burdacontractor.feature.deliveryorder.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.core.utils.withDateFormat
import com.android.burdacontractor.databinding.FragmentFilterDeliveryOrderListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker

class FilterDeliveryOrderFragment(private val createdByOrFor: CreatedByOrFor, private val dateStart: String?, private val dateEnd: String?) : BottomSheetDialogFragment() {
    private var onClickListener: OnClickListener? = null
    private var currentButtonCreatedByOrFor: CreatedByOrFor = CreatedByOrFor.all
    private var currentDateStart: String? = null
    private var currentDateEnd: String? = null
    interface OnClickListener {
        fun onClickListener(createdByOrFor: CreatedByOrFor, dateStart: String?, dateEnd: String?)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterDeliveryOrderListDialogBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.RoundedBackgroundBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentButtonCreatedByOrFor = createdByOrFor
        currentDateStart = dateStart
        currentDateEnd = dateEnd
        _binding = FragmentFilterDeliveryOrderListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCurrentButtonCreatedByOrFor(currentButtonCreatedByOrFor)
        setCurrentDateStartAndEnd(dateStart,dateEnd)
        binding.btnChooseDate.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText("Pilih Rentang Tanggal")
            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {selection ->
                currentDateStart = getDateFromMillis(selection.first, "yyyy/MM/dd")
                currentDateEnd = getDateFromMillis(selection.second, "yyyy/MM/dd")
                setCurrentDateStartAndEnd(currentDateStart,currentDateEnd)
            }
            datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        }
        binding.btnForAll.setOnClickListener {
            currentButtonCreatedByOrFor = CreatedByOrFor.all
            setCurrentButtonCreatedByOrFor(currentButtonCreatedByOrFor)
        }
        binding.btnForSelf.setOnClickListener {
            currentButtonCreatedByOrFor = CreatedByOrFor.self
            setCurrentButtonCreatedByOrFor(currentButtonCreatedByOrFor)
        }
        binding.btnResetDate.setOnClickListener {
            currentDateStart = null
            currentDateEnd = null
            binding.tvRangeDate.text = getString(R.string.tanggal_belum_diatur)
            binding.btnResetDate.setGone()
        }
        binding.btnAtur.setOnClickListener {
            onClickListener?.onClickListener(
                currentButtonCreatedByOrFor, currentDateStart, currentDateEnd
            )
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
                currentDateStart = start
                currentDateEnd = end
                val formattedStartDate = start.withDateFormat()
                val formattedEndDate = end.withDateFormat()
                binding.tvRangeDate.text = getString(R.string.range_date, formattedStartDate, formattedEndDate)
            }
        }?: binding.btnResetDate.setGone()
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {

        val TAG = FilterDeliveryOrderFragment::class.java.simpleName
        fun newInstance(createdByOrFor: CreatedByOrFor, dateStart: String?, dateEnd: String?): FilterDeliveryOrderFragment {
            val fragment = FilterDeliveryOrderFragment(createdByOrFor, dateStart, dateEnd)
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