package com.android.burdacontractor.feature.deliveryorder.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.*
import com.android.burdacontractor.databinding.ActivityDeliveryOrderDetailBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderCetakActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryOrderDetailBinding
    private val storageViewModel: StorageViewModel by viewModels()
    private val deliveryOrderDetailViewModel: DeliveryOrderDetailViewModel by viewModels()
    private var deliveryOrderDetail: DeliveryOrderDetail? = null
    private var snackbar: Snackbar? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deliveryOrderDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DELIVERY_ORDER, DeliveryOrderDetail::class.java)
        }else{
            intent.getParcelableExtra(DELIVERY_ORDER)
        }
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderDetailViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }
    }
    private fun initObserver(){
        deliveryOrderDetailViewModel.getDeliveryOrderById(deliveryOrderDetail?.id.toString())
        deliveryOrderDetailViewModel.deliveryOrder.observe(this){ deliveryOrder->
            initLayout(storageViewModel.role, deliveryOrder)
            initUi(deliveryOrder)
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
    private fun initUi(deliveryOrder: DeliveryOrderDetail){
        if(deliveryOrder.logisticNama!!.isNotBlank() && deliveryOrder.kendaraanMerk!!.isNotBlank()){
            with(binding){
                layoutKendaraan.setVisible()
                tvMerkKendaraan.text = deliveryOrder.kendaraanMerk
                tvPlatKendaraan.text = deliveryOrder.kendaraanPlatNomor
                tvNamaDriver.text = deliveryOrder.logisticNama
                ivDriver.setImageFromUrl(deliveryOrder.logisticFoto.toString(),this@DeliveryOrderCetakActivity)
                btnHubungiDriver.setOnClickListener {
                    Intent(Intent.ACTION_DIAL).data = Uri.parse("tel:${deliveryOrder.logisticNoHp}")
                }
            }
        }
        with(binding){
            btnBack.setOnClickListener { finish() }
            btnDelete.setOnClickListener {
//                deliveryOrderDetailViewModel.deleteDeliveryOrder(deliveryOrder.id)
            }
            btnDownload.setOnClickListener {
                openActivityWithExtras(DeliveryOrderCetakActivity::class.java,this@DeliveryOrderCetakActivity){
                    intent.putExtra("HAHAHA", deliveryOrder)
                }
            }
            tvAlamatAsal.text = deliveryOrder.tempatAsalAlamat
            tvNamaAsal.text = deliveryOrder.tempatAsalNama
            tvAlamatTujuan.text = deliveryOrder.tempatTujuanAlamat
            ivPurchasing.setImageFromUrl(deliveryOrder.purchasingFoto.toString(),this@DeliveryOrderCetakActivity)
        }
    }
    private fun initLayout(userRole: String, deliveryOrder: DeliveryOrderDetail){
        when(userRole){
            UserRole.ADMIN_GUDANG.name ->{
                when(deliveryOrder.status){
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_ADMIN_GUDANG.name -> {
                        binding.btnPilihDriver.setVisible()
                        binding.btnDelete.setVisible()
                        setButtonStyle(binding.btnPilihDriver,true)
                    }
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                        binding.btnDownload.setVisible()
                        binding.btnUbahDriver.setVisible()
                        setButtonStyle(binding.btnUbahDriver,false)
                        binding.btnPantauLokasi.setVisible()
                        setButtonStyle(binding.btnPilihDriver,true)
                    }
                    DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                        binding.btnDownload.setVisible()
                        binding.btnUbahDriver.setVisible()
                        setButtonStyle(binding.btnUbahDriver,false)
                        binding.btnPantauLokasi.setVisible()
                        setButtonStyle(binding.btnPilihDriver,true)
                    }
                    DeliveryOrderStatus.SELESAI.name -> {
                        binding.btnDownload.setVisible()
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
                    DeliveryOrderStatus.MENUNGGU_KONFIRMASI_ADMIN_GUDANG.name -> {

                    }
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
        val DELIVERY_ORDER = "deliveryOrder"
    }
}