package com.android.burdacontractor.feature.profile.presentation.users

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
import com.android.burdacontractor.databinding.FragmentFilterUsersDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener


class FilterUsersFragment : BottomSheetDialogFragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()
    private var onClickListener: OnClickListener? = null
    private var roleIndex: Int? = null

    interface OnClickListener {
        fun onClickListener()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private var _binding: FragmentFilterUsersDialogBinding? = null
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
        _binding = FragmentFilterUsersDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            usersViewModel.listRole.observe(viewLifecycleOwner) { listRole ->
                spinnerRole.setItems(listRole)
                usersViewModel.roleIndex.value?.let {
                    spinnerRole.selectItemByIndex(it)
                }
                btnResetRole.setOnClickListener {
                    roleIndex = null
                    spinnerRole.clearSelectedItem()
                    spinnerRole.setItems(emptyList<String>())
                    spinnerRole.setItems(listRole)
                    btnResetRole.setGone()
                }
            }
            spinnerRole.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                roleIndex = newIndex
                btnResetRole.setVisible()
            })
            btnAtur.setOnClickListener {
                usersViewModel.setRoleIndex(roleIndex)
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

        val TAG = FilterUsersFragment::class.java.simpleName
        fun newInstance(): FilterUsersFragment {
            val fragment = FilterUsersFragment()
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