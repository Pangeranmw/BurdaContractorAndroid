package com.android.burdacontractor.feature.profile.presentation

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.checkPassword
import com.android.burdacontractor.core.utils.confirmPassword
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.databinding.ActivityUpdatePasswordBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding
    private val updatePasswordViewModel: UpdatePasswordViewModel by viewModels()
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            R.string.no_internet,
            Snackbar.LENGTH_INDEFINITE
        )
        updatePasswordViewModel.liveNetworkChecker.observe(this) {
            this.checkConnection(snackbar, it) { initObserver() }
        }
        setContentView(binding.root)
    }

    private fun initObserver() {
        updatePasswordViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updatePasswordViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        initUi()
    }

    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }

    private fun initUi() {
        onBackPressedCallback()
        binding.btnUpdate.setOnClickListener {
            if (isInputCorrect()) validateUpdate()
        }
        binding.etPasswordLama.doAfterTextChanged {
            checkPassword(it.toString(), binding.etPasswordLamaLayout)
            isInputCorrect()
        }
        binding.etPasswordBaru.doAfterTextChanged {
            checkPassword(it.toString(), binding.etPasswordBaruLayout)
            isInputCorrect()
        }
        binding.etKonfirmasiPasswordBaru.doAfterTextChanged {
            confirmPassword(
                it.toString(),
                binding.etPasswordBaru.text.toString(),
                binding.etKonfirmasiPasswordBaruLayout
            )
            isInputCorrect()
        }
    }

    private fun validateUpdate() {
        updatePasswordViewModel.changePassword(
            binding.etPasswordLama.text.toString(),
            binding.etPasswordBaru.text.toString(),
        ) {
            binding.etPasswordBaru.text = null
            binding.etKonfirmasiPasswordBaru.text = null
            binding.etPasswordLama.text = null
        }
    }

    private fun isInputCorrect(): Boolean {
        binding.btnUpdate.isEnabled =
            TextUtils.isEmpty(binding.etPasswordBaruLayout.error)
                    && TextUtils.isEmpty(binding.etPasswordLamaLayout.error)
                    && TextUtils.isEmpty(binding.etKonfirmasiPasswordBaruLayout.error)
                    && binding.etPasswordBaru.text.toString().isNotEmpty()
                    && binding.etPasswordLama.text.toString().isNotEmpty()
                    && binding.etKonfirmasiPasswordBaru.text.toString().isNotEmpty()
        return binding.btnUpdate.isEnabled
    }
}