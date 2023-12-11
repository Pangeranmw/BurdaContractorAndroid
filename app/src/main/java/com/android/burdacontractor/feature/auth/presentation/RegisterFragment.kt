package com.android.burdacontractor.feature.auth.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.checkEmail
import com.android.burdacontractor.core.utils.checkNumber
import com.android.burdacontractor.core.utils.checkPassword
import com.android.burdacontractor.core.utils.emptyData
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setToastShort
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            R.string.no_internet,
            Snackbar.LENGTH_INDEFINITE
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.liveNetworkChecker.observe(viewLifecycleOwner) {
            requireContext().checkConnection(snackbar, it) { initObserver() }
        }
        initUI()
    }

    private fun initObserver() {
        authViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    requireView().findNavController()
                        .navigate(R.id.action_registerFragment_to_loginAccountFragment)
                }
            }
        }
    }

    private fun initUI() {
        binding.etEmail.doAfterTextChanged {
            requireContext().checkEmail(it.toString(), binding.etEmailLayout)
            isInputCorrect()
        }
        binding.etPassword.doAfterTextChanged {
            requireContext().checkPassword(it.toString(), binding.etPasswordLayout)
            isInputCorrect()
        }
        binding.etNama.doAfterTextChanged {
            requireContext().emptyData(it.toString(), binding.etNamaLayout)
            isInputCorrect()
        }
        binding.etNoHp.doAfterTextChanged {
            requireContext().checkNumber(it.toString(), binding.etNoHpLayout)
            isInputCorrect()
        }
        binding.btnRegister.setOnClickListener {
            if (isInputCorrect()) validateRegister()
            else requireActivity().setToastShort("Data Masih Keliru")
        }
        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginAccountFragment)
        }
    }

    private fun validateRegister() {
        authViewModel.register(
            binding.etNama.text.toString(),
            binding.etNoHp.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

    private fun isInputCorrect(): Boolean {
        binding.btnRegister.isEnabled = TextUtils.isEmpty(binding.etPasswordLayout.error)
                && TextUtils.isEmpty(binding.etEmailLayout.error)
                && TextUtils.isEmpty(binding.etNamaLayout.error)
                && TextUtils.isEmpty(binding.etNoHpLayout.error)
                && binding.etEmail.text.toString().isNotEmpty()
                && binding.etPassword.text.toString().isNotEmpty()
                && binding.etNama.text.toString().isNotEmpty()
                && binding.etNoHp.text.toString().isNotEmpty()
        return binding.btnRegister.isEnabled
    }
}