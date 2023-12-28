package com.android.burdacontractor.core.presentation.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.setToastShort
import com.skydoves.powerspinner.PowerSpinnerView

class UpdateRoleDialog(
    private val canTouchOutside: Boolean = true,
    private val spinnerValue: String,
    private val subtitle: String? = null,
    private val title: String? = null,
    private val blockMainButton: ((String) -> Unit)? = null,
    private val blockSecondaryButton: (() -> Unit)? = null,
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        val view = inflater.inflate(R.layout.dialog_update_role, container, false)
        val mainButton: Button = view.findViewById(R.id.main_button_dialog_role)

        dialog?.setCanceledOnTouchOutside(canTouchOutside)
        isCancelable = canTouchOutside

        val secondaryButton: Button = view.findViewById(R.id.secondary_button_dialog_role)

        val tvTitle: TextView = view.findViewById(R.id.tv_title_dialog_role)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle_dialog_role)

        if (title == null) tvTitle.isGone = true
        else tvTitle.text = title

        if (subtitle == null) tvSubtitle.isGone = true
        else tvSubtitle.text = subtitle

        val spinner: PowerSpinnerView = view.findViewById(R.id.spinner_update_role)
        var selectedRole: String? = null
        spinner.text = spinnerValue
        spinner.setItems(
            listOf(
                UserRole.USER.name,
                UserRole.LOGISTIC.name,
                UserRole.SUPERVISOR.name,
                UserRole.PROJECT_MANAGER.name,
                UserRole.SITE_MANAGER.name,
                UserRole.ADMIN.name,
                UserRole.ADMIN_GUDANG.name,
                UserRole.PURCHASING.name,
            )
        )

        spinner.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            selectedRole = newItem
        }

        secondaryButton.setOnClickListener {
            blockSecondaryButton?.let { it() }
            dismiss()
        }
        mainButton.setOnClickListener {
            selectedRole?.let { role ->
                blockMainButton?.let { it(role) }
            } ?: requireContext().setToastShort("Anda Belum Memilih Role")
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}