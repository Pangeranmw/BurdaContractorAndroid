package com.android.burdacontractor.feature.deliveryorder.presentation.update

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.DataMapper.combineLogisticWithKendaraanSimpleToAllLogistic
import com.android.burdacontractor.core.utils.DataMapper.tempatSimpleToAllGudang
import com.android.burdacontractor.core.utils.DataMapper.tempatSimpleToAllPerusahaan
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.core.utils.customBackPressed
import com.android.burdacontractor.core.utils.finishAction
import com.android.burdacontractor.core.utils.getDateFromMillis
import com.android.burdacontractor.core.utils.parcelable
import com.android.burdacontractor.databinding.ActivityAddDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticViewModel
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateDeliveryOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDeliveryOrderBinding
    private val updateDeliveryOrderViewModel: UpdateDeliveryOrderViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private val pilihPerusahaanViewModel: PilihPerusahaanViewModel by viewModels()
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by viewModels()
    private val pilihGudangViewModel: PilihGudangViewModel by viewModels()
    private val pilihLogisticViewModel: PilihLogisticViewModel by viewModels()
    private var deliveryOrder: DeliveryOrderDetailItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeliveryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        deliveryOrder = intent.parcelable(Constant.INTENT_PARCEL)
        updateDeliveryOrderViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        val sectionsPagerAdapter = UpdateDeliveryOrderPagerAdapter(this@UpdateDeliveryOrderActivity)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tvAppBar.text = getString(R.string.ubah_delivery_order)
        pilihPerusahaanViewModel.setPerusahaan(tempatSimpleToAllPerusahaan(deliveryOrder!!.perusahaan))
        pilihGudangViewModel.setGudang(tempatSimpleToAllGudang(deliveryOrder!!.gudang))
        pilihLogisticViewModel.setLogistic(
            combineLogisticWithKendaraanSimpleToAllLogistic(
                deliveryOrder!!.logistic,
                deliveryOrder!!.kendaraan
            )
        )
        updateDeliveryOrderViewModel.setPerihal(deliveryOrder!!.perihal)
        updateDeliveryOrderViewModel.setTglPengambilan(
            getDateFromMillis(
                deliveryOrder!!.tglPengambilan,
                "MM/dd/yyyy"
            )
        )
        updateDeliveryOrderViewModel.setUntukPerhatian(deliveryOrder!!.untukPerhatian)
        updateDeliveryOrderViewModel.setDeliveryOrderId(deliveryOrder!!.id)
        updateDeliveryOrderViewModel.setListPreOrder(deliveryOrder!!.preOrder!!)

        updateDeliveryOrderViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        updateDeliveryOrderViewModel.messageResponse.observe(this) {
            it.getContentIfNotHandled()?.let { messageResponse ->
                Snackbar.make(
                    binding.root,
                    messageResponse,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        pilihLogisticViewModel.logistic.observe(this) { logistic ->
            pilihKendaraanViewModel.kendaraan.observe(this) { kendaraan ->
                pilihPerusahaanViewModel.perusahaan.observe(this) { perusahaan ->
                    pilihGudangViewModel.gudang.observe(this) { gudang ->
                        updateDeliveryOrderViewModel.perihal.observe(this) { perihal ->
                            updateDeliveryOrderViewModel.tglPengambilan.observe(this) { tglPengambilan ->
                                updateDeliveryOrderViewModel.untukPerhatian.observe(this) { untukPerhatian ->
                                    if (logistic != null &&
                                        kendaraan != null &&
                                        perusahaan != null &&
                                        gudang != null &&
                                        perihal != null &&
                                        tglPengambilan != null &&
                                        untukPerhatian != null
                                    ) {
                                        if (tglPengambilan.isNotBlank() && perihal.isNotBlank() && untukPerhatian.isNotBlank()) {
                                            updateDeliveryOrderViewModel.setCanSwipe(true)
                                        }
                                    } else {
                                        updateDeliveryOrderViewModel.setCanSwipe(false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        onBackPressedCallback()
        updateDeliveryOrderViewModel.canSwipe.observe(this) {
            binding.viewPager.isUserInputEnabled = it
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
                tab.view.isEnabled = it
            }.attach()
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    updateDeliveryOrderViewModel.updateCreateStepOneDo(
                        logisticId = pilihLogisticViewModel.logistic.value!!.id,
                        purchasingId = storageViewModel.userId,
                        perusahaanId = pilihPerusahaanViewModel.perusahaan.value!!.id,
                        gudangId = pilihGudangViewModel.gudang.value!!.id,
                        kendaraanId = pilihKendaraanViewModel.kendaraan.value!!.id,
                        perihal = updateDeliveryOrderViewModel.perihal.value!!,
                        tglPengambilan = updateDeliveryOrderViewModel.tglPengambilan.value!!,
                        untukPerhatian = updateDeliveryOrderViewModel.untukPerhatian.value!!
                    )
                }
                onBackPressedCallback()
            }
        })
    }

    fun onBackPressedCallback() {
        if (binding.viewPager.currentItem == 0) {
            binding.btnBack.setOnClickListener { finishAction() }
            customBackPressed()
        } else if (binding.viewPager.currentItem == 1) {
            binding.btnBack.setOnClickListener { binding.viewPager.currentItem = 0 }
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding.viewPager.currentItem = 0
                }
            })
        }
    }
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.data_delivery_order,
            R.string.data_pre_order
        )
    }
}