package com.android.burdacontractor.feature.deliveryorder.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListPreOrderAdapter
import com.android.burdacontractor.core.utils.*
import com.android.burdacontractor.databinding.ActivityDeliveryOrderDetailBinding
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryOrderDetailBinding
    private lateinit var poAdapter: ListPreOrderAdapter
    private val storageViewModel: StorageViewModel by viewModels()
    private val deliveryOrderDetailViewModel: DeliveryOrderDetailViewModel by viewModels()
    private lateinit var id: String
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(ID_DELIVERY_ORDER).toString()
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderDetailViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }


    }
    private fun initObserver(){
        deliveryOrderDetailViewModel.getDeliveryOrderById(id)
        deliveryOrderDetailViewModel.deliveryOrder.observe(this){ deliveryOrder->
            initLayout(storageViewModel.role, deliveryOrder)
            initUi(deliveryOrder)
        }
        deliveryOrderDetailViewModel.state.observe(this){
            when(it){
                StateResponse.LOADING -> binding.srLayout.isRefreshing = true
                StateResponse.ERROR -> binding.srLayout.isRefreshing = false
                StateResponse.SUCCESS -> {
                    binding.srLayout.isRefreshing = false
                }
                else -> {}
            }
        }
    }
    private fun setButtonStyle(button: AppCompatButton, isPrimary: Boolean){
        if(isPrimary){
            button.background = AppCompatResources.getDrawable(this, R.drawable.semi_rounded_primary)
            button.setTextColor(AppCompatResources.getColorStateList(this, R.color.white))
        }else{
            button.background = AppCompatResources.getDrawable(this, R.drawable.semi_rounded_outline_main)
            button.setTextColor(AppCompatResources.getColorStateList(this, R.color.primary_main))
        }
    }
    private fun initUi(deliveryOrder: DeliveryOrderDetailItem){
        with(binding){
            poAdapter = ListPreOrderAdapter()
            poAdapter.submitList(deliveryOrder.preOrder)
            rvPreOrder.layoutManager = GridLayoutManager(this@DeliveryOrderDetailActivity,2,GridLayoutManager.VERTICAL,false)
            rvPreOrder.adapter = poAdapter

            tvKodeDo.text = deliveryOrder.kodeDo
            tvStatus.text = enumValueToNormal(deliveryOrder.status)
            when(deliveryOrder.status){
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(this@DeliveryOrderDetailActivity,R.color.orange_dark_full))
                }
                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(this@DeliveryOrderDetailActivity,R.color.red))
                }
                DeliveryOrderStatus.SELESAI.name -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(this@DeliveryOrderDetailActivity,R.color.secondary_main))
                }
            }
            tvCreatedAt.text = getDateFromMillis(deliveryOrder.createdAt)
            tvUpdatedAt.text = getDateFromMillis(deliveryOrder.updatedAt)

            tvAlamatAsal.text = deliveryOrder.tempatAsal.alamat
            tvNamaAsal.text = deliveryOrder.tempatAsal.nama
            tvNamaTujuan.text = deliveryOrder.tempatTujuan.nama
            tvAlamatTujuan.text = deliveryOrder.tempatTujuan.alamat

            tvTglPengambilan.text = getDateFromMillis(deliveryOrder.tglPengambilan)
            ivPurchasing.setImageFromUrl(deliveryOrder.purchasing.foto,this@DeliveryOrderDetailActivity)
            tvNoHpPurchasing.text = deliveryOrder.purchasing.noHp
            tvNamaPurchasing.text = deliveryOrder.purchasing.nama

            tvPerihal.text = deliveryOrder.perihal
            tvUntukPerhatian.text = deliveryOrder.untukPerhatian

            ivKendaraan.setImageFromUrl(deliveryOrder.kendaraan.gambar,this@DeliveryOrderDetailActivity)
            tvMerkKendaraan.text = deliveryOrder.kendaraan.merk
            tvPlatKendaraan.text = deliveryOrder.kendaraan.platNomor

            tvNamaDriver.text = deliveryOrder.logistic.nama
            tvNoHpDriver.text = deliveryOrder.logistic.noHp
            ivDriver.setImageFromUrl(deliveryOrder.logistic.foto,this@DeliveryOrderDetailActivity)

            ivTtd.setImageFromUrl(deliveryOrder.ttd, this@DeliveryOrderDetailActivity)
            tvTertandaPemohon.text = deliveryOrder.purchasing.nama
            tvRoleTertandaPemohon.text = enumValueToNormal(deliveryOrder.purchasing.role)

            btnWaDriver.setOnClickListener {
                it.openWhatsAppChat(deliveryOrder.logistic.noHp)
            }
            btnHubungiDriver.setOnClickListener {
                dialIntent(deliveryOrder.logistic.noHp)
            }
            btnWaPurchasing.setOnClickListener {
                it.openWhatsAppChat(deliveryOrder.purchasing.noHp)
            }
            btnHubungiPurchasing.setOnClickListener {
                dialIntent(deliveryOrder.purchasing.noHp)
            }
            btnBack.setOnClickListener {
                finish()
                overridePendingTransition(0,0)
            }
            btnDelete.setOnClickListener {
//                deliveryOrderDetailViewModel.deleteDeliveryOrder(deliveryOrder.id)
            }
            btnDownload.setOnClickListener {
                openActivityWithExtras(DeliveryOrderCetakActivity::class.java,false){
                    putString(DeliveryOrderCetakActivity.ID_DELIVERY_ORDER, deliveryOrder.id)
                    putString(DeliveryOrderCetakActivity.KODE_DELIVERY_ORDER, deliveryOrder.kodeDo)
                }
            }
        }
    }
    private fun initLayout(userRole: String, deliveryOrder: DeliveryOrderDetailItem){
        when(userRole){
            UserRole.ADMIN_GUDANG.name ->{
                when(deliveryOrder.status){
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                        binding.btnPantauLokasi.setVisible()
                    }
                    DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                        binding.btnPantauLokasi.setVisible()
                    }
                    DeliveryOrderStatus.SELESAI.name -> {
                        binding.layoutButton.setGone()
                    }
                }
            }
            UserRole.LOGISTIC.name ->{
                when(deliveryOrder.status){
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {

                    }
                    DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {

                    }
                    DeliveryOrderStatus.SELESAI.name -> {

                    }
                }
            }
            UserRole.PURCHASING.name -> {
                when(deliveryOrder.status){
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {

                    }
                    DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {

                    }
                    DeliveryOrderStatus.SELESAI.name -> {

                    }
                }
            }
        }
    }
    companion object{
        const val ID_DELIVERY_ORDER = "deliveryOrderId"
    }
}