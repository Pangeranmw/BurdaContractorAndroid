package com.android.burdacontractor.presentation.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.StateResponse
import com.android.burdacontractor.core.domain.model.UserRole
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentHomeBinding
import com.android.burdacontractor.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val authViewModel: AuthViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            checkConnection(it)
        }
    }

    private fun initObserver(){
        authViewModel.loginMessageResponse.observe(viewLifecycleOwner){
            if(it!=null){
                Snackbar.make(binding.root,it ,Snackbar.LENGTH_INDEFINITE).show()
            }
        }
        authViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                else -> binding.progressBar.setGone()
            }
        }
        authViewModel.loginResponse.observe(viewLifecycleOwner){
            if(it!=null){
                UserRole.values().forEach { role->
                    if(role.name == it.role)
                        authViewModel.loginUser(it.id,it.token,role)
                }
            }
        }
    }
    private fun checkConnection(state: Boolean){
        if(!state){
            snackbar = Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
            snackbar?.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.error_color))
            snackbar?.show()
        }else{
            snackbar?.setText(R.string.connected)
            snackbar?.setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.green_light_full))
            snackbar?.dismiss()
            initObserver()
        }
    }
}