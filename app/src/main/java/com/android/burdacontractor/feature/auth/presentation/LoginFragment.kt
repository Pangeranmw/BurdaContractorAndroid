package com.android.burdacontractor.feature.auth.presentation

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.LogisticViewModel
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
    private val authViewModel: AuthViewModel by viewModels()
    private val logisticViewModel: LogisticViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
//    private val id = "050bc5de-6222-3b8e-b946-1c1ea1922318"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        logisticViewModel.getCoordinate(id)
    }
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
        authViewModel.messageResponse.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        authViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    requireContext().openActivity(BerandaActivity::class.java, requireActivity())
                }
                else -> {}
            }
        }
        logisticViewModel.logisticCoordinate.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    binding.progressBar.setVisible()
                }
                is Resource.Error -> {
                    binding.progressBar.setGone()
                    Log.d("Error", it.message.toString())
                }
                is Resource.Success -> {
                    binding.progressBar.setGone()
                    if(it.data!=null){
//                        binding.etEmail.setText(it.data.latitude.toString())
//                        binding.etPassword.setText(it.data.longitude.toString())
//
//                        binding.tvEmail.text = (it.data.latitude.toString())
//                        binding.tvPassword.text = (it.data.longitude.toString())
                    }
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
    }
    private fun validateLogin(){
        authViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
    }
    private fun isInputCorrect(): Boolean{
        binding.btnLogin.isEnabled = TextUtils.isEmpty(binding.etPasswordLayout.error) && TextUtils.isEmpty(binding.etEmailLayout.error) && binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()
        return binding.btnLogin.isEnabled
    }
}