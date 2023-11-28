package com.android.burdacontractor.core.presentation.customview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.setImageFromUrl

class CustomDialog(
    private val mainButtonText: String? = null,
    private val mainButtonBackgroundDrawable: Int? = null,
    private val secondaryButtonText: String? = null,
    private val secondaryButtonTextColor: Int? = null,
    private val mainButtonTextColor: Int? = null,
    private val secondaryButtonBackgroundDrawable: Int? = null,
    private val title: String? = null,
    private val subtitle: String? = null,
    private val image: Drawable? = null,
    private val imageUrl: String? = null,
    private val canTouchOutside: Boolean = true,
    private val blockMainButton: (() -> Unit)? = null,
    private val blockSecondaryButton: (() -> Unit)? = null,
): DialogFragment() {
    private lateinit var mainButton: Button
    private lateinit var secondaryButton: Button
    private lateinit var ivDialog: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        val view = inflater.inflate(R.layout.dialaogue_two_button, container, false)
        mainButton = view.findViewById(R.id.main_button_dialog)
        if (mainButtonBackgroundDrawable != null)
            mainButton.setBackgroundResource(mainButtonBackgroundDrawable)

        dialog?.setCanceledOnTouchOutside(canTouchOutside)
        isCancelable = canTouchOutside

        secondaryButton = view.findViewById(R.id.secondary_button_dialog)
        if (secondaryButtonBackgroundDrawable != null)
            secondaryButton.setBackgroundResource(secondaryButtonBackgroundDrawable)

        if (secondaryButtonTextColor != null)
            secondaryButton.setTextColor(requireContext().getColor(secondaryButtonTextColor))

        if (mainButtonTextColor != null)
            mainButton.setTextColor(requireContext().getColor(mainButtonTextColor))

        tvTitle = view.findViewById(R.id.tv_title_dialog)
        tvSubtitle = view.findViewById(R.id.tv_subtitle_dialog)
        ivDialog = view.findViewById(R.id.iv_dialog)

        if (title == null) tvTitle.isGone = true
        else tvTitle.text = title

        if (subtitle == null) tvSubtitle.isGone = true
        else tvSubtitle.text = subtitle

        if (image == null && imageUrl == null) ivDialog.isGone = true
        else {
            if (image != null) {
                ivDialog.setImageDrawable(image)
            } else {
                ivDialog.setImageFromUrl(imageUrl, requireContext())
            }
        }

        if (secondaryButtonText == null) secondaryButton.isGone = true
        else {
            secondaryButton.text = secondaryButtonText
            secondaryButton.setOnClickListener {
                blockSecondaryButton?.let { it() }
                dismiss()
            }
        }

        if(mainButtonText==null) mainButton.isGone = true
        else {
            mainButton.text = mainButtonText
            mainButton.setOnClickListener {
                blockMainButton?.let { it() }
                dismiss()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}