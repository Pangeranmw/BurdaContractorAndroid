package com.android.burdacontractor.core.presentation.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.Tema
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.setTheme
import com.android.burdacontractor.core.utils.setToastShort
import com.skydoves.powerspinner.PowerSpinnerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateTemaDialog : DialogFragment() {
    private val storageViewModel: StorageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        val view = inflater.inflate(R.layout.dialog_change_theme, container, false)
        val mainButton: Button = view.findViewById(R.id.main_button_dialog_tema)
        val secondaryButton: Button = view.findViewById(R.id.secondary_button_dialog_tema)
        val spinner: PowerSpinnerView = view.findViewById(R.id.spinner_update_tema)
        var selectedTheme: Tema? = null
        spinner.text = storageViewModel.theme.ifBlank { Tema.Sistem.name }
        spinner.setItems(
            listOf(
                Tema.Sistem.name,
                Tema.Terang.name,
                Tema.Gelap.name,
            )
        )

        spinner.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            selectedTheme = Tema.valueOf(newItem)
        }

        secondaryButton.setOnClickListener {
            dismiss()
        }
        mainButton.setOnClickListener {
            selectedTheme?.let { tema ->
                setTheme(tema.name)
                storageViewModel.setTheme(tema)
            } ?: requireContext().setToastShort("Anda Belum Memilih Tema")
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