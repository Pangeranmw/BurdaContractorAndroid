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
import com.android.burdacontractor.core.utils.checkEmail
import com.android.burdacontractor.core.utils.checkNumber
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.databinding.ActivityUpdateProfileBinding
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private val updateProfileViewModel: UpdateProfileViewModel by viewModels()
    private var snackbar: Snackbar? = null
    private lateinit var user: UserByTokenItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            R.string.no_internet,
            Snackbar.LENGTH_INDEFINITE
        )
        updateProfileViewModel.liveNetworkChecker.observe(this) {
            this.checkConnection(snackbar, it) { initObserver() }
        }
        setContentView(binding.root)
    }

    private fun initObserver() {
        updateProfileViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updateProfileViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        updateProfileViewModel.user.observe(this) { user ->
            this.user = user
            initProfile()
            initUi()
        }
    }

    private fun initProfile() {
        binding.etEmail.setText(user.email)
        binding.etNama.setText(user.nama)
        binding.etNoHp.setText(user.noHp)
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
        binding.etEmail.doAfterTextChanged {
            checkEmail(it.toString(), binding.etEmailLayout)
            isInputCorrect()
        }
        binding.etNama.doAfterTextChanged {
            emptyData(it.toString(), binding.etNamaLayout)
            isInputCorrect()
        }
        binding.etNoHp.doAfterTextChanged {
            checkNumber(it.toString(), binding.etNoHpLayout)
            isInputCorrect()
        }
    }

    private fun validateUpdate() {
        updateProfileViewModel.updateProfile(
            binding.etNama.text.toString(),
            binding.etEmail.text.toString(),
            binding.etNoHp.text.toString(),
        ) {
            finish()
        }
    }

    private fun isInputCorrect(): Boolean {
        binding.btnUpdate.isEnabled =
            TextUtils.isEmpty(binding.etEmailLayout.error)
                    && TextUtils.isEmpty(binding.etNamaLayout.error)
                    && TextUtils.isEmpty(binding.etNoHpLayout.error)
                    && binding.etEmail.text.toString().isNotEmpty()
                    && binding.etNama.text.toString().isNotEmpty()
                    && binding.etNoHp.text.toString().isNotEmpty()
        return binding.btnUpdate.isEnabled
    }
}