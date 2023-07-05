package com.android.burdacontractor.feature.beranda.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.utils.*
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
import com.android.burdacontractor.feature.suratjalan.presentation.SuratJalanDetailActivity
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
    private var snackbar: Snackbar? = null

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
        snackbar=Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        berandaViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            requireContext().checkConnection(snackbar,it){ initObserver() }
        }
    }

    private fun initObserver() {
        berandaViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.progressBar.setVisible()
                StateResponse.ERROR -> binding.progressBar.setGone()
                StateResponse.SUCCESS -> {
                    binding.progressBar.setGone()
                    binding.srLayout.isRefreshing = false
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
                initAdapterSjPengembalian()
                adapterSjPengembalian.setListSuratJalan(sjPengembalian.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengembalian.text = sjPengembalian.count.toString()
                if(sjPengembalian.count!!>0) binding.tvCountSjPengembalian.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            }
            berandaViewModel.sjPengirimanGp.observe(viewLifecycleOwner){sjPengirimanGp->
                initAdapterSjPengirimanGp()
                adapterSjPengirimanGp.setListSuratJalan(sjPengirimanGp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengirimanGp.text = sjPengirimanGp.count.toString()
                if(sjPengirimanGp.count!!>0) binding.tvCountSjPengirimanGp.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            }
            berandaViewModel.sjPengirimanPp.observe(viewLifecycleOwner){sjPengirimanPp->
                initAdapterSjPengirimanPp()
                adapterSjPengirimanPp.setListSuratJalan(sjPengirimanPp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengirimanPp.text = sjPengirimanPp.count.toString()
                if(sjPengirimanPp.count!!>0) binding.tvCountSjPengirimanPp.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            }
//            berandaViewModel.deliveryOrder.observe(viewLifecycleOwner){deliveryOrder->
//                adapterDeliveryOrder.setListDeliveryOrder(deliveryOrder.suratJalan!!,.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name} user.role)
//                binding.tvCountDeliveryOrder.text = deliveryOrder.count.toString()
//            }
        }
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
        binding.ivUser.setOnClickListener{
            requireContext().openActivity(ProfileActivity::class.java,requireActivity())
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
    private fun initAdapterDeliveryOrder(){
        adapterDeliveryOrder = ListSuratJalanAdapter()
        binding.rvDeliveryOrder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDeliveryOrder.setHasFixedSize(true)
        binding.rvDeliveryOrder.adapter = adapterDeliveryOrder
    }
    private fun initAdapterSjPengirimanPp(){
        adapterSjPengirimanPp = ListSuratJalanAdapter()
        binding.rvSjPengirimanPp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSjPengirimanPp.setHasFixedSize(true)
        binding.rvSjPengirimanPp.adapter = adapterSjPengirimanPp
    }
    private fun initAdapterDoDalamPerjalanan(){
        adapterDoDalamPerjalanan = ListSuratJalanAdapter()
        binding.rvDoDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvDoDalamPerjalanan.setHasFixedSize(true)
        binding.rvDoDalamPerjalanan.adapter = adapterDoDalamPerjalanan
    }
    private fun initAdapterSjDalamPerjalanan(){
        adapterSjDalamPerjalanan = ListSuratJalanAdapter()
        binding.rvSjDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSjDalamPerjalanan.setHasFixedSize(true)
        binding.rvSjDalamPerjalanan.adapter = adapterSjDalamPerjalanan
    }
    private fun initAdapterSjPengembalian(){
        adapterSjPengembalian = ListSuratJalanAdapter()
        binding.rvSjPengembalian.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSjPengembalian.setHasFixedSize(true)
        binding.rvSjPengembalian.adapter = adapterSjPengembalian
    }
    private fun initAdapterSjPengirimanGp(){
        adapterSjPengirimanGp = ListSuratJalanAdapter()
        binding.rvSjPengirimanGp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSjPengirimanGp.setHasFixedSize(true)
        binding.rvSjPengirimanGp.adapter = adapterSjPengirimanGp
    }
    private fun initUi(){
        binding.srLayout.setOnRefreshListener {
            berandaViewModel.getSomeActiveSuratJalanUseCase(SuratJalanTipe.PENGEMBALIAN)
            berandaViewModel.getSomeActiveSuratJalanUseCase(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
            berandaViewModel.getSomeActiveSuratJalanUseCase(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
            berandaViewModel.getUserByToken()
        }
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