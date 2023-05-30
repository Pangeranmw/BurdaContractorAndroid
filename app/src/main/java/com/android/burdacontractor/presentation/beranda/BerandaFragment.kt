package com.android.burdacontractor.presentation.beranda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.android.burdacontractor.presentation.LogisticViewModel
import com.android.burdacontractor.presentation.auth.AuthViewModel
import com.android.burdacontractor.presentation.auth.LoginActivity
import com.android.burdacontractor.presentation.service.location.LocationService
import com.android.burdacontractor.presentation.splashscreen.StorageViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BerandaFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            checkConnection(it)
        }
    }

    private fun initObserver() {
        authViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    Intent(requireActivity(), LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP
                        requireActivity().stopService(this)
                    }
                    requireActivity().openActivity(LoginActivity::class.java, requireActivity())
                }
                else -> {}
            }
        }
        authViewModel.messageResponse.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun initUi(){
        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
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
            initUi()
        }
    }
}