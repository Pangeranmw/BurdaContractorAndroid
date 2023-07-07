package com.android.burdacontractor.feature.beranda.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.*
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.android.burdacontractor.feature.profile.presentation.ProfileActivity
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
    private lateinit var adapterSjDalamPerjalanan: ListSuratJalanAdapter
    private lateinit var adapterDeliveryOrder: ListDeliveryOrderAdapter
    private lateinit var adapterDoDalamPerjalanan: ListDeliveryOrderAdapter
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
            berandaViewModel.sjDalamPerjalanan.observe(viewLifecycleOwner){sjDalamPerjalanan->
                initAdapterSjDalamPerjalanan()
                adapterSjDalamPerjalanan.setListSuratJalan(sjDalamPerjalanan, user.role)
                val count = sjDalamPerjalanan.size
                binding.tvCountSjDalamPerjalanan.text = count.toString()
                if(count>0){
                    binding.tvCountSjDalamPerjalanan.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
            berandaViewModel.doDalamPerjalanan.observe(viewLifecycleOwner){doDalamPerjalanan->
                initAdapterDoDalamPerjalanan()
                adapterDoDalamPerjalanan.setListDeliveryOrder(doDalamPerjalanan, user.role)
                val count = doDalamPerjalanan.size
                binding.tvCountDoDalamPerjalanan.text = count.toString()
                if(count>0){
                    binding.tvCountDoDalamPerjalanan.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
            berandaViewModel.sjPengembalian.observe(viewLifecycleOwner){sjPengembalian->
                initAdapterSjPengembalian()
                adapterSjPengembalian.setListSuratJalan(sjPengembalian.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengembalian.text = sjPengembalian.count.toString()
                if(sjPengembalian.count!!>0){
                    binding.btnSeeAllSjPengembalian.setVisible()
                    binding.tvCountSjPengembalian.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
            berandaViewModel.sjPengirimanGp.observe(viewLifecycleOwner){sjPengirimanGp->
                initAdapterSjPengirimanGp()
                adapterSjPengirimanGp.setListSuratJalan(sjPengirimanGp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengirimanGp.text = sjPengirimanGp.count.toString()
                if(sjPengirimanGp.count!!>0){
                    binding.btnSeeAllSjPengirimanGp.setVisible()
                    binding.tvCountSjPengirimanGp.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
            berandaViewModel.sjPengirimanPp.observe(viewLifecycleOwner){sjPengirimanPp->
                initAdapterSjPengirimanPp()
                adapterSjPengirimanPp.setListSuratJalan(sjPengirimanPp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengirimanPp.text = sjPengirimanPp.count.toString()
                if(sjPengirimanPp.count!!>0){
                    binding.btnSeeAllSjPengirimanPp.setVisible()
                    binding.tvCountSjPengirimanPp.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
            berandaViewModel.kendaraanByLogistic.observe(viewLifecycleOwner){
                if(it==null) {
                    binding.cvKendaraan.setGone()
                    binding.tvEmptyKendaraan.setVisible()
                }else{
                    binding.tvEmptyKendaraan.setGone()
                    binding.cvKendaraan.setVisible()
                    binding.tvGudangKendaraan.text = it.namaGudang
                    binding.tvAlamatKendaraan.text = it.alamatGudang
                    binding.tvMerkKendaraan.text = it.merk
                    binding.tvPlatKendaraan.text = it.platNomor
                    Glide.with(this)
                        .load(getPhotoUrl(it.gambar))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.ivKendaraan)
                    when(it.jenis){
                        JenisKendaraan.MINIBUS.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_minibus)
                        JenisKendaraan.MOBIL.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_car)
                        JenisKendaraan.MOTOR.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_motorcycle)
                        JenisKendaraan.PICKUP.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_pickup)
                        JenisKendaraan.TRONTON.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_tronton)
                        JenisKendaraan.TRUCK.name -> binding.ivJenisKendaraan.setImageResource(R.drawable.vehicle_truck_box)
                    }
                }
            }
            berandaViewModel.deliveryOrder.observe(viewLifecycleOwner){deliveryOrder->
                initAdapterDeliveryOrder()
                adapterDeliveryOrder.setListDeliveryOrder(deliveryOrder.deliveryOrder!!.filterNot{it.status == DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name}, user.role)
                binding.tvCountSjPengirimanPp.text = deliveryOrder.count.toString()
                if(deliveryOrder.count!!>0){
                    binding.btnSeeAllDeliveryOrder.setVisible()
                    binding.tvCountDeliveryOrder.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                }
            }
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
            requireContext().openActivity(ProfileActivity::class.java,requireActivity(), false)
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
        adapterDeliveryOrder = ListDeliveryOrderAdapter()
        binding.rvDeliveryOrder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDeliveryOrder.setHasFixedSize(true)
        binding.rvDeliveryOrder.adapter = adapterDeliveryOrder
    }
    private fun initAdapterSjPengirimanPp(){
        adapterSjPengirimanPp = ListSuratJalanAdapter()
        binding.rvSjPengirimanPp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengirimanPp.setHasFixedSize(true)
        binding.rvSjPengirimanPp.adapter = adapterSjPengirimanPp
    }
    private fun initAdapterDoDalamPerjalanan(){
        adapterDoDalamPerjalanan = ListDeliveryOrderAdapter()
        binding.rvDoDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDoDalamPerjalanan.setHasFixedSize(true)
        binding.rvDoDalamPerjalanan.adapter = adapterDoDalamPerjalanan
    }
    private fun initAdapterSjDalamPerjalanan(){
        adapterSjDalamPerjalanan = ListSuratJalanAdapter()
        binding.rvSjDalamPerjalanan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjDalamPerjalanan.setHasFixedSize(true)
        binding.rvSjDalamPerjalanan.adapter = adapterSjDalamPerjalanan
    }
    private fun initAdapterSjPengembalian(){
        adapterSjPengembalian = ListSuratJalanAdapter()
        binding.rvSjPengembalian.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengembalian.setHasFixedSize(true)
        binding.rvSjPengembalian.adapter = adapterSjPengembalian
    }
    private fun initAdapterSjPengirimanGp(){
        adapterSjPengirimanGp = ListSuratJalanAdapter()
        binding.rvSjPengirimanGp.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSjPengirimanGp.setHasFixedSize(true)
        binding.rvSjPengirimanGp.adapter = adapterSjPengirimanGp
    }
    private fun initUi(){
        binding.srLayout.setOnRefreshListener {
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGEMBALIAN)
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
            berandaViewModel.getUserByToken()
            berandaViewModel.getKendaraanByLogistic()
            berandaViewModel.getAllSuratJalanDalamPerjalananByUser()
        }
    }
}