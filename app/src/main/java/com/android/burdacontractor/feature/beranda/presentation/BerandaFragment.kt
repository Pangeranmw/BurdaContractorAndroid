package com.android.burdacontractor.feature.beranda.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BerandaFragment : Fragment() {
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private val berandaViewModel: BerandaViewModel by viewModels()
    private lateinit var adapterSjPengembalian: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanGp: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanPp: ListSuratJalanAdapter
    private lateinit var adapterDeliveryOrder: ListSuratJalanAdapter
    private lateinit var adapterDoDalamPerjalanan: ListSuratJalanAdapter
    private lateinit var adapterSjDalamPerjalanan: ListSuratJalanAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        binding.layoutDeliveryOrder.setGone()
        binding.layoutKendaraan.setGone()
        binding.layoutSjPengembalian.setGone()
        binding.layoutSjPengirimanGp.setGone()
        binding.layoutSjPengirimanPp.setGone()
        binding.layoutSjDalamPerjalanan.setGone()
        binding.layoutDoDalamPerjalanan.setGone()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        berandaViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            requireContext().checkConnection(it, binding.root){ initObserver() }
        }
    }

    private fun initObserver() {
        berandaViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    // Logout
//                    Intent(requireActivity(), LocationService::class.java).apply {
//                        action = LocationService.ACTION_STOP
//                        requireActivity().stopService(this)
//                    }
//                    requireActivity().openActivity(LoginActivity::class.java, requireActivity())
                }
                else -> {}
            }
        }
        berandaViewModel.messageResponse.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        berandaViewModel.user.observe(viewLifecycleOwner){user->
            initAppBar(user)
            initLayout(user)
            berandaViewModel.sjPengembalian.observe(viewLifecycleOwner){sjPengembalian->
                adapterSjPengembalian.setListSuratJalan(sjPengembalian.suratJalan!!, user.role)
                binding.tvCountSjPengembalian.text = sjPengembalian.count.toString()
            }
            berandaViewModel.sjPengirimanGp.observe(viewLifecycleOwner){sjPengirimanGp->
                adapterSjPengirimanGp.setListSuratJalan(sjPengirimanGp.suratJalan!!, user.role)
                binding.tvCountSjPengirimanGp.text = sjPengirimanGp.count.toString()
            }
            berandaViewModel.sjPengirimanPp.observe(viewLifecycleOwner){sjPengirimanPp->
                adapterSjPengirimanPp.setListSuratJalan(sjPengirimanPp.suratJalan!!, user.role)
                binding.tvCountSjPengirimanPp.text = sjPengirimanPp.count.toString()
            }
//            berandaViewModel.deliveryOrder.observe(viewLifecycleOwner){deliveryOrder->
//                adapterDeliveryOrder.setListDeliveryOrder(deliveryOrder.suratJalan!!, user.role)
//                binding.tvCountDeliveryOrder.text = deliveryOrder.count.toString()
//            }
        }
        initAdapter()
        initUi()
    }
    private fun initAppBar(user: User){
        binding.tvNamaUser.text = user.nama
        binding.tvRoleUser.text = enumValueToNormal(user.role)
        if(user.foto !=null){
            Glide.with(this)
                .load(getPhotoUrl(user.foto))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivUser)
        }
    }
    private fun initLayout(user: User){
        when(user.role){
            UserRole.PURCHASING.name -> {
                binding.layoutDeliveryOrder.setVisible()
                binding.layoutDoDalamPerjalanan.setVisible()
            }
            UserRole.LOGISTIC.name -> {
                binding.layoutKendaraan.setVisible()
                binding.layoutDeliveryOrder.setVisible()
                binding.layoutDoDalamPerjalanan.setVisible()
                binding.layoutSjPengembalian.setVisible()
                binding.layoutSjPengirimanGp.setVisible()
                binding.layoutSjPengirimanPp.setVisible()
                binding.layoutSjDalamPerjalanan.setVisible()
            }
            UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                binding.layoutDeliveryOrder.setVisible()
                binding.layoutDoDalamPerjalanan.setVisible()
                binding.layoutSjPengembalian.setVisible()
                binding.layoutSjPengirimanGp.setVisible()
                binding.layoutSjPengirimanPp.setVisible()
                binding.layoutSjDalamPerjalanan.setVisible()
            }
            UserRole.SUPERVISOR.name, UserRole.PROJECT_MANAGER.name -> {
                binding.layoutSjPengembalian.setVisible()
                binding.layoutSjPengirimanGp.setVisible()
                binding.layoutSjPengirimanPp.setVisible()
                binding.layoutSjDalamPerjalanan.setVisible()
            }
        }
    }
    private fun initAdapter(){
        adapterSjPengembalian = ListSuratJalanAdapter()
        binding.rvSjPengembalian.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengembalian.setHasFixedSize(true)
        binding.rvSjPengembalian.adapter = adapterSjPengembalian

        adapterSjPengirimanGp = ListSuratJalanAdapter()
        binding.rvSjPengirimanGp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengirimanGp.setHasFixedSize(true)
        binding.rvSjPengirimanGp.adapter = adapterSjPengirimanGp

        adapterSjPengirimanPp = ListSuratJalanAdapter()
        binding.rvSjPengirimanPp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengirimanPp.setHasFixedSize(true)
        binding.rvSjPengirimanPp.adapter = adapterSjPengirimanPp

        adapterDeliveryOrder = ListSuratJalanAdapter()
        binding.rvDeliveryOrder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDeliveryOrder.setHasFixedSize(true)
        binding.rvDeliveryOrder.adapter = adapterDeliveryOrder

        adapterDoDalamPerjalanan = ListSuratJalanAdapter()
        binding.rvDoDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDoDalamPerjalanan.setHasFixedSize(true)
        binding.rvDoDalamPerjalanan.adapter = adapterDoDalamPerjalanan

        adapterSjDalamPerjalanan = ListSuratJalanAdapter()
        binding.rvSjDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjDalamPerjalanan.setHasFixedSize(true)
        binding.rvSjDalamPerjalanan.adapter = adapterSjDalamPerjalanan
    }
    private fun initUi(){

//        adapter = NoteAdapter()
//
//        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
//        binding?.rvNotes?.setHasFixedSize(true)
//        binding?.rvNotes?.adapter = adapter

//        binding.btnLogout.setOnClickListener {
//            authViewModel.logout()
//        }
    }
    private fun setAdapter(search: String? = null){
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