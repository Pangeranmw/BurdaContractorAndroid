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
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentForgetPasswordBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
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
                        .navigate(R.id.action_forgetPasswordFragment_to_loginAccountFragment)
                }
            }
        }
    }

    private fun initUI() {
        binding.etEmail.doAfterTextChanged {
            requireContext().checkEmail(it.toString(), binding.etEmailLayout)
            isInputCorrect()
        }
        binding.btnSubmit.setOnClickListener {
            validateLogin()
        }
        binding.tvLogin.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_forgetPasswordFragment_to_loginAccountFragment)
        }
    }

    private fun validateLogin() {
        authViewModel.forgetPassword(binding.etEmail.text.toString())
    }

    private fun isInputCorrect(): Boolean {
        binding.btnSubmit.isEnabled =
            TextUtils.isEmpty(binding.etEmailLayout.error) && binding.etEmail.text.toString()
                .isNotEmpty()
        return binding.btnSubmit.isEnabled
    }
}