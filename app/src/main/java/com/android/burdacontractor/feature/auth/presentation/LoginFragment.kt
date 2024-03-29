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
import com.android.burdacontractor.core.utils.checkPassword
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentLoginBinding
import com.android.burdacontractor.feature.beranda.presentation.BerandaActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        snackbar=Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            requireContext().checkConnection(snackbar,it){ initObserver() }
        }
        initUI()
    }

    private fun initObserver(){
        authViewModel.state.observe(viewLifecycleOwner){
            when (it) {
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                }
            }
        }
    }
    private fun initUI(){
        binding.etEmail.doAfterTextChanged {
            requireContext().checkEmail(it.toString(), binding.etEmailLayout)
            isInputCorrect()
        }
        binding.etPassword.doAfterTextChanged {
            requireContext().checkPassword(it.toString(), binding.etPasswordLayout)
            isInputCorrect()
        }
        binding.btnLogin.setOnClickListener {
            validateLogin()
        }
        binding.tvRegister.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }
        binding.tvLupaPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }
    private fun validateLogin() {
        authViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString()) {
            requireActivity().openActivity(BerandaActivity::class.java)
        }
    }
    private fun isInputCorrect(): Boolean{
        binding.btnLogin.isEnabled = TextUtils.isEmpty(binding.etPasswordLayout.error) && TextUtils.isEmpty(binding.etEmailLayout.error) && binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()
        return binding.btnLogin.isEnabled
    }
}