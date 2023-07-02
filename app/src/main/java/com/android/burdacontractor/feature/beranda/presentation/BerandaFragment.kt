package com.android.burdacontractor.feature.beranda.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.service.location.LocationService
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.android.burdacontractor.feature.auth.presentation.AuthViewModel
import com.android.burdacontractor.feature.auth.presentation.LoginActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanFragment
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BerandaFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private var snackbar: Snackbar? = null
    private val suratJalanViewModel: SuratJalanViewModel by viewModels()
    private lateinit var adapter: ListSuratJalanAdapter
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
            requireContext().checkConnection(it, binding.root){ initObserver() }
        }
    }

    private fun initObserver() {
        authViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    // Logout
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
        initUi()
    }
    private fun initUi(){
//        binding.btnLogout.setOnClickListener {
//            authViewModel.logout()
//        }
    }
    private fun setAdapter(search: String? = null){
//        if(adapter!=null) adapter.refresh()
        adapter = ListSuratJalanAdapter{
            val mBundle = Bundle()
            mBundle.putString(SuratJalanFragment.SURAT_JALAN_ID, it.id)
            findNavController().navigate(R.id.action_surat_jalan_fragment_to_detailSuratJalanFragment, mBundle)
        }
//        binding.rvSuratJalan.adapter = adapter
//        suratJalanViewModel.getAllSuratJalan(
//            tipe,
//            status,
//            dateStart,
//            dateEnd,
//            10,
//            search,
//        ).observe(viewLifecycleOwner) {
//            adapter.submitData(lifecycle, it)
//        }
    }
}