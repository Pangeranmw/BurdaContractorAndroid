package com.android.burdacontractor.feature.deliveryorder.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.Constant.INTENT_PARCEL
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.presentation.adapter.ListPreOrderAdapter
import com.android.burdacontractor.core.presentation.customview.CustomDialog
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.dialIntent
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.openWhatsAppChat
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.ActivityDeliveryOrderDetailBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.presentation.location.PantauLokasiDeliveryOrderActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.location.TelusuriLokasiDeliveryOrderActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.print.DeliveryOrderCetakActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.update.UpdateDeliveryOrderActivity
import com.android.burdacontractor.feature.deliveryorder.presentation.uploadphoto.UploadFotoBuktiDeliveryOrderActivity
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryOrderDetailBinding
    private lateinit var poAdapter: ListPreOrderAdapter
    private val deliveryOrderDetailViewModel: DeliveryOrderDetailViewModel by viewModels()
    private lateinit var id: String
    private var deliveryOrder: DeliveryOrderDetailItem? = null
    private var user: UserByTokenItem? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra(Constant.INTENT_ID).toString()
        snackbar = Snackbar.make(binding.mainLayout,getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        deliveryOrderDetailViewModel.liveNetworkChecker.observe(this){
            checkConnection(snackbar,it){ initObserver() }
        }
    }
    private fun initObserver(){
        deliveryOrderDetailViewModel.id.observe(this){
            if(it==null) deliveryOrderDetailViewModel.setId(id)
        }
        deliveryOrderDetailViewModel.deliveryOrder.observe(this){ deliveryOrder->
            this.deliveryOrder = deliveryOrder
            deliveryOrderDetailViewModel.user.observe(this){ user->
                this.user = user
                initLayout()
                initUi()
            }
        }
        deliveryOrderDetailViewModel.state.observe(this){
            binding.srLayout.isRefreshing = it==StateResponse.LOADING
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
    override fun onRestart() {
        super.onRestart()
        refreshData()
    }
    private fun refreshData(){
        deliveryOrderDetailViewModel.getDeliveryOrderById(id)
    }
    private fun initUi(){
        with(binding){
            poAdapter = ListPreOrderAdapter(false, {}, {})
            poAdapter.submitList(deliveryOrder!!.preOrder)
            rvPreOrder.layoutManager = GridLayoutManager(this@DeliveryOrderDetailActivity,2,GridLayoutManager.VERTICAL,false)
            rvPreOrder.adapter = poAdapter

            tvKodeDo.text = deliveryOrder!!.kodeDo
            tvStatus.text = enumValueToNormal(deliveryOrder!!.status)
            when(deliveryOrder!!.status){
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    tvStatus.setTextColor(ContextCompat.getColor(this@DeliveryOrderDetailActivity,R.color.orange_dark_full))
                }

                DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                    tvStatus.setTextColor(
                        ContextCompat.getColor(
                            this@DeliveryOrderDetailActivity,
                            R.color.red
                        )
                    )
                }

                DeliveryOrderStatus.SELESAI.name -> {
                    tvStatus.setTextColor(
                        ContextCompat.getColor(
                            this@DeliveryOrderDetailActivity,
                            R.color.secondary_main
                        )
                    )
                }
            }
            tvCreatedAt.text = getDateFromMillis(deliveryOrder!!.createdAt)
            tvUpdatedAt.text = getDateFromMillis(deliveryOrder!!.updatedAt)

            tvAlamatAsal.text = deliveryOrder!!.gudang.alamat
            tvNamaAsal.text = deliveryOrder!!.gudang.nama
            tvNamaTujuan.text = deliveryOrder!!.perusahaan.nama
            tvAlamatTujuan.text = deliveryOrder!!.perusahaan.alamat

            tvTglPengambilan.text =
                getDateFromMillis(deliveryOrder!!.tglPengambilan, "dd MMMM yyyy")

            ivPurchasing.setImageFromUrl(
                deliveryOrder!!.purchasing.foto,
                this@DeliveryOrderDetailActivity
            )
            if (deliveryOrder?.purchasing?.foto != null) {
                ivPurchasing.layoutParams.width = 100
                ivPurchasing.layoutParams.height = 100
                ivPurchasing.requestLayout()
            }
            tvNoHpPurchasing.text = deliveryOrder!!.purchasing.noHp
            tvNamaPurchasing.text = deliveryOrder!!.purchasing.nama

            tvPerihal.text = deliveryOrder!!.perihal
            tvUntukPerhatian.text = deliveryOrder!!.untukPerhatian

            ivKendaraan.setImageFromUrl(deliveryOrder!!.kendaraan.gambar,this@DeliveryOrderDetailActivity)
            if(deliveryOrder?.kendaraan?.gambar!=null){
                ivKendaraan.layoutParams.width = 100
                ivKendaraan.layoutParams.height = 100
                ivKendaraan.requestLayout()
            }
            tvMerkKendaraan.text = deliveryOrder!!.kendaraan.merk
            tvPlatKendaraan.text = deliveryOrder!!.kendaraan.platNomor

            tvNamaDriver.text = deliveryOrder!!.logistic.nama
            tvNoHpDriver.text = deliveryOrder!!.logistic.noHp
            ivDriver.setImageFromUrl(deliveryOrder!!.logistic.foto,this@DeliveryOrderDetailActivity)
            if(deliveryOrder?.logistic?.foto!=null){
                ivDriver.layoutParams.width = 100
                ivDriver.layoutParams.height = 100
                ivDriver.requestLayout()
            }

            ivTtd.setImageFromUrl(deliveryOrder!!.ttd, this@DeliveryOrderDetailActivity)
            tvTertandaPemohon.text = deliveryOrder!!.purchasing.nama
            tvRoleTertandaPemohon.text = enumValueToNormal(deliveryOrder!!.purchasing.role)

            btnWaDriver.setOnClickListener {
                it.openWhatsAppChat(deliveryOrder!!.logistic.noHp)
            }
            btnHubungiDriver.setOnClickListener {
                dialIntent(deliveryOrder!!.logistic.noHp)
            }

            btnWaPurchasing.setOnClickListener {
                it.openWhatsAppChat(deliveryOrder!!.purchasing.noHp)
            }
            btnHubungiPurchasing.setOnClickListener {
                dialIntent(deliveryOrder!!.purchasing.noHp)
            }

            deliveryOrder?.fotoBukti?.let{
                layoutFotoBukti.setVisible()
                ivFotoBukti.setImageFromUrl(it, this@DeliveryOrderDetailActivity, false)
            }

            if(deliveryOrder?.adminGudang!=null){
                layoutPenandaSelesai.setVisible()
                ivAdminGudang.setImageFromUrl(deliveryOrder!!.adminGudang!!.foto,this@DeliveryOrderDetailActivity)
                if(deliveryOrder?.adminGudang?.foto!=null){
                    ivAdminGudang.layoutParams.width = 100
                    ivAdminGudang.layoutParams.height = 100
                    ivAdminGudang.requestLayout()
                }
                tvNoHpAdminGudang.text = deliveryOrder!!.adminGudang!!.noHp
                tvNamaAdminGudang.text = deliveryOrder!!.adminGudang!!.nama
                btnWaAdminGudang.setOnClickListener {
                    it.openWhatsAppChat(deliveryOrder!!.adminGudang!!.noHp)
                }
                btnHubungiAdminGudang.setOnClickListener {
                    dialIntent(deliveryOrder!!.adminGudang!!.noHp)
                }
            }
            btnDownload.setOnClickListener {
                openActivityWithExtras(DeliveryOrderCetakActivity::class.java,false){
                    putString(Constant.INTENT_ID, deliveryOrder!!.id)
                    putString(Constant.INTENT_KODE, deliveryOrder!!.kodeDo)
                }
            }
            srLayout.setOnRefreshListener {
                refreshData()
            }
            onBackPressedCallback()
        }
    }
    private fun onBackPressedCallback() {
        binding.btnBack.setOnClickListener { finishAction() }
        customBackPressed()
    }
    private fun initLayout(){
        with(binding){

            if (user!!.role == UserRole.LOGISTIC.name && (deliveryOrder!!.logistic.id != user!!.id)) {
                CustomDialog(
                    mainButtonText = "Keluar",
                    mainButtonBackgroundDrawable = null,
                    secondaryButtonText = null,
                    secondaryButtonTextColor = null,
                    mainButtonTextColor = null,
                    secondaryButtonBackgroundDrawable = null,
                    title = "Tidak Bisa Melihat Delivery Order",
                    subtitle = "Delivery order ini sudah bukan untuk anda",
                    canTouchOutside = false,
                    image = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_error),
                    blockMainButton = { finish() },
                    blockSecondaryButton = {}).show(supportFragmentManager, "DOUnavailable")
            }
            // Hilangkan button hubungi pada data diri sendiri
            if (deliveryOrder!!.purchasing.id == user!!.id)
                layoutHubungiPurchasing.setGone()
            if (deliveryOrder!!.adminGudang?.id == user!!.id)
                layoutHubungiAdminGudang.setGone()
            if (deliveryOrder!!.logistic.id == user!!.id)
                layoutHubungiDriver.setGone()

            when (user!!.role) {
                UserRole.ADMIN_GUDANG.name, UserRole.PURCHASING.name, UserRole.ADMIN.name -> {
                    btnPantauLokasi.setOnClickListener {
                        openActivityWithExtras(PantauLokasiDeliveryOrderActivity::class.java,false){
                            putParcelable(INTENT_PARCEL, deliveryOrder)
                        }
                    }
                }
            }
            when(deliveryOrder!!.status){
                DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                    when(user!!.role){
                        UserRole.ADMIN_GUDANG.name, UserRole.PURCHASING.name, UserRole.ADMIN.name -> {
                            btnTandaiSelesai.setVisible()
                            btnTandaiSelesai.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Selesai",
                                    secondaryButtonText = "Batal",
                                    title = "Tandai Selesai Delivery Order",
                                    subtitle = "Apakah anda yakin ingin menandai selesai delivery order ${deliveryOrder!!.kodeDo} ?",
                                    image = null,
                                    blockMainButton = {
                                        deliveryOrderDetailViewModel.markCompleteDeliveryOrder(
                                            deliveryOrder!!.id
                                        ) {
                                            refreshData()
                                        }
                                    },
                                    blockSecondaryButton = {}
                                ).show(supportFragmentManager, "MarkCompleteFragment")
                            }
                        }
                    }
                }

                DeliveryOrderStatus.SELESAI.name -> {
                    layoutButton.setGone()
                }
            }
            when(user!!.role){
                UserRole.LOGISTIC.name ->{
                    btnPantauLokasi.setGone()
                    btnTelusuriLokasi.setVisible()
                    btnTelusuriLokasi.setOnClickListener {
                        openActivityWithExtras(
                            TelusuriLokasiDeliveryOrderActivity::class.java,
                            false
                        ) {
                            putParcelable(INTENT_PARCEL, deliveryOrder)
                        }
                    }
                    btnAmbilBarang.setGone()
                    when(deliveryOrder!!.status){
                        DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            btnAmbilBarang.setVisible()
                            btnAmbilBarang.setOnClickListener {
                                CustomDialog(
                                    mainButtonText = "Ambil",
                                    secondaryButtonText = "Batal",
                                    title = "Ambil Barang",
                                    subtitle = "Apakah anda yakin ingin mengambil delivery order ${deliveryOrder!!.kodeDo} ?",
                                    image = null,
                                    blockMainButton = {
                                        deliveryOrderDetailViewModel.sendDeliveryOrder(deliveryOrder!!.id) {
                                            refreshData()
                                        }
                                    },
                                    blockSecondaryButton = {}
                                ).show(supportFragmentManager, "SendDoFragment")
                            }
                        }

                        DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN.name -> {
                            btnUploadFotoBukti.setVisible()
                            btnUploadFotoBukti.setOnClickListener{
                                openActivityWithExtras(UploadFotoBuktiDeliveryOrderActivity::class.java,false){
                                    putParcelable(UploadFotoBuktiDeliveryOrderActivity.DELIVERY_ORDER, deliveryOrder)
                                }
                            }
                        }
                    }
                }

                UserRole.PURCHASING.name, UserRole.ADMIN_GUDANG.name, UserRole.ADMIN.name -> {
                    when (deliveryOrder!!.status) {
                        DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER.name -> {
                            if (deliveryOrder!!.purchasing.id == user!!.id) {
                                btnUbahDo.setVisible()
                                btnDelete.setVisible()
                                btnUbahDo.setOnClickListener {
                                    openActivityWithExtras(
                                        UpdateDeliveryOrderActivity::class.java,
                                        false
                                    ) {
                                        putParcelable(INTENT_PARCEL, deliveryOrder!!)
                                    }
                                }
                                btnDelete.setOnClickListener {
                                    CustomDialog(
                                        mainButtonText = "Ya",
                                        secondaryButtonText = "Tidak",
                                        secondaryButtonBackgroundDrawable = R.drawable.semi_rounded_outline_red,
                                        secondaryButtonTextColor = R.color.red,
                                        mainButtonBackgroundDrawable = R.drawable.semi_rounded_red,
                                        title = "Hapus Delivery Order",
                                        subtitle = "Apakah anda yakin ingin menghapus delivery order ${deliveryOrder!!.kodeDo} ?",
                                        image = null,
                                        blockMainButton = {
                                            deliveryOrderDetailViewModel.deleteDeliveryOrder(
                                                deliveryOrder!!.id
                                            ) {
                                                finish()
                                            }
                                        },
                                        blockSecondaryButton = {}).show(
                                        supportFragmentManager,
                                        "DeleteDo"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}