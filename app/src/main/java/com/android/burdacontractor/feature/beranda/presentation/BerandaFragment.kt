package com.android.burdacontractor.feature.beranda.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListDeliveryOrderAdapter
import com.android.burdacontractor.core.presentation.adapter.ListStatistikMenungguSuratJalanAdapter
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.openActivity
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentBerandaBinding
import com.android.burdacontractor.feature.deliveryorder.presentation.DeliveryOrderDetailActivity
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
    private val storageViewModel: StorageViewModel by viewModels()
    private lateinit var adapterStatSJ: ListStatistikMenungguSuratJalanAdapter
    private lateinit var adapterSjPengembalian: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanGp: ListSuratJalanAdapter
    private lateinit var adapterSjPengirimanPp: ListSuratJalanAdapter
    private lateinit var adapterSjDalamPerjalanan: ListSuratJalanAdapter
    private lateinit var adapterDeliveryOrder: ListDeliveryOrderAdapter
    private lateinit var adapterDoDalamPerjalanan: ListDeliveryOrderAdapter
    private var internetConnectionSnackbar: Snackbar? = null

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
        binding.layoutMenungguSuratJalan.setGone()
        internetConnectionSnackbar=Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        berandaViewModel.user.observe(viewLifecycleOwner){user->
            storageViewModel.updateUser(user)
            berandaViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
                requireContext().checkConnection(internetConnectionSnackbar,it){ initObserver() }
            }
        }
    }

    private fun initObserver() {
        berandaViewModel.state.observe(viewLifecycleOwner){
            when(it){
                StateResponse.LOADING -> binding.srLayout.isRefreshing = true
                StateResponse.ERROR -> binding.srLayout.isRefreshing = false
                StateResponse.SUCCESS -> {
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
        berandaViewModel.sjDalamPerjalanan.observe(viewLifecycleOwner){sjDalamPerjalanan->
            adapterSjDalamPerjalanan = initAdapterSj()
            initRvSj(binding.rvSjDalamPerjalanan,adapterSjDalamPerjalanan)
            adapterSjDalamPerjalanan.submitList(sjDalamPerjalanan)
            initCountTextAndButton(sjDalamPerjalanan.size, binding.tvCountSjDalamPerjalanan)
        }
        berandaViewModel.doDalamPerjalanan.observe(viewLifecycleOwner){doDalamPerjalanan->
            adapterDoDalamPerjalanan = initAdapterDeliveryOrder()
            initRvDo(binding.rvDoDalamPerjalanan,adapterDoDalamPerjalanan)
            adapterDoDalamPerjalanan.submitList(doDalamPerjalanan)
            initCountTextAndButton(doDalamPerjalanan.size, binding.tvCountDoDalamPerjalanan)
        }
        if(storageViewModel.role==UserRole.ADMIN.name || storageViewModel.role==UserRole.ADMIN_GUDANG.name){
            berandaViewModel.statistikMenungguSuratJalan.observe(viewLifecycleOwner){stat->
                adapterStatSJ = ListStatistikMenungguSuratJalanAdapter()
                adapterStatSJ.submitList(stat)
                binding.rvMenungguSuratJalan.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                binding.rvMenungguSuratJalan.adapter = adapterStatSJ
            }
        }
        berandaViewModel.sjPengembalian.observe(viewLifecycleOwner){sjPengembalian->
            adapterSjPengembalian = initAdapterSj()
            initRvSj(binding.rvSjPengembalian,adapterSjPengembalian)
            adapterSjPengembalian.submitList(sjPengembalian.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengembalian.count!!,binding.tvCountSjPengembalian,binding.btnSeeAllSjPengembalian)
        }
        berandaViewModel.sjPengirimanGp.observe(viewLifecycleOwner){sjPengirimanGp->
            adapterSjPengirimanGp = initAdapterSj()
            initRvSj(binding.rvSjPengirimanGp,adapterSjPengirimanGp)
            adapterSjPengirimanGp.submitList(sjPengirimanGp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengirimanGp.count!!,binding.tvCountSjPengirimanGp,binding.btnSeeAllSjPengirimanGp)
        }
        berandaViewModel.sjPengirimanPp.observe(viewLifecycleOwner){sjPengirimanPp->
            adapterSjPengirimanPp = initAdapterSj()
            initRvSj(binding.rvSjPengirimanPp,adapterSjPengirimanPp)
            adapterSjPengirimanPp.submitList(sjPengirimanPp.suratJalan!!.filterNot{it.status == SuratJalanStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(sjPengirimanPp.count!!,binding.tvCountSjPengirimanPp,binding.btnSeeAllSjPengirimanPp)
        }
        berandaViewModel.deliveryOrder.observe(viewLifecycleOwner){deliveryOrder->
            adapterDeliveryOrder = initAdapterDeliveryOrder()
            initRvDo(binding.rvDeliveryOrder,adapterDeliveryOrder)
            adapterDeliveryOrder.submitList(deliveryOrder.deliveryOrder!!.filterNot{it.status == DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name})
            initCountTextAndButton(deliveryOrder.count!!,binding.tvCountDeliveryOrder,binding.btnSeeAllDeliveryOrder)
        }
        getViewModelData()
        initAppBar()
        initLayout()
        initUi()
    }
    private fun initCountTextAndButton(count: Int, tvCount: TextView, btn: Button? = null){
        tvCount.text = count.toString()
        if(count>0){
            tvCount.maxWidth = 240
            tvCount.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
            btn?.setVisible()
        }
    }
    private fun initAppBar(){
        binding.tvNamaUser.text = storageViewModel.name
        binding.tvRoleUser.text = enumValueToNormal(storageViewModel.role)
        if(storageViewModel.photo.isNotBlank()){
            Glide.with(this)
                .load(getPhotoUrl(storageViewModel.photo))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivUser)
        }
        binding.ivUser.setOnClickListener{
            requireActivity().openActivity(ProfileActivity::class.java, false)
        }
    }
    private fun initLayout(){
        when(storageViewModel.role){
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
                binding.layoutMenungguSuratJalan.setVisible()
            }
            UserRole.SUPERVISOR.name, UserRole.SITE_MANAGER.name, UserRole.PROJECT_MANAGER.name -> {
                binding.layoutSjPengembalian.setVisible()
                binding.layoutSjPengirimanGp.setVisible()
                binding.layoutSjPengirimanPp.setVisible()
                binding.layoutSjDalamPerjalanan.setVisible()
            }
        }
    }
    private fun initAdapterDeliveryOrder(): ListDeliveryOrderAdapter{
        return ListDeliveryOrderAdapter(storageViewModel.role, storageViewModel.userId){
            requireActivity().openActivityWithExtras(DeliveryOrderDetailActivity::class.java,false) {
                putString(DeliveryOrderDetailActivity.ID_DELIVERY_ORDER, it.id)
            }
        }
    }
    private fun initAdapterSj(): ListSuratJalanAdapter{
        return ListSuratJalanAdapter(storageViewModel.role, storageViewModel.userId) {
            requireActivity().openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                putString(SuratJalanDetailActivity.ID_SURAT_JALAN, it.id)
            }
        }
    }
    private fun initRvSj(rv: RecyclerView, adapter: ListSuratJalanAdapter){
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
    private fun initRvDo(rv: RecyclerView, adapter: ListDeliveryOrderAdapter){
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = adapter
    }
    private fun initUi(){
        binding.srLayout.setOnRefreshListener {
            getViewModelData()
        }
    }
    override fun onResume() {
        super.onResume()
        getViewModelData()
    }
    private fun getViewModelData(){
        if(storageViewModel.role==UserRole.LOGISTIC.name ||
            storageViewModel.role==UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role==UserRole.ADMIN.name ||
            storageViewModel.role==UserRole.PURCHASING.name
        ) {
            berandaViewModel.getSomeActiveDeliveryOrder()
            berandaViewModel.getAllDeliveryOrderDalamPerjalananByUser()
        }

        if(storageViewModel.role==UserRole.LOGISTIC.name ||
            storageViewModel.role==UserRole.ADMIN_GUDANG.name ||
            storageViewModel.role==UserRole.ADMIN.name ||
            storageViewModel.role==UserRole.SUPERVISOR.name ||
            storageViewModel.role==UserRole.PROJECT_MANAGER.name ||
            storageViewModel.role==UserRole.SITE_MANAGER.name
        ) {
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGEMBALIAN)
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
            berandaViewModel.getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
            berandaViewModel.getAllSuratJalanDalamPerjalananByUser()
        }
        if(storageViewModel.role==UserRole.ADMIN_GUDANG.name || storageViewModel.role==UserRole.ADMIN.name) {
            berandaViewModel.getStatistikMenungguSuratJalan()
        }
        if(storageViewModel.role==UserRole.LOGISTIC.name){
            berandaViewModel.getKendaraanByLogistic()
        }
        (activity as BerandaActivity).refreshBadgeValue()
    }
}