package com.android.burdacontractor.feature.deliveryorder.presentation.create

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.databinding.ActivityAddDeliveryOrderBinding
import com.android.burdacontractor.feature.gudang.presentation.PilihGudangViewModel
import com.android.burdacontractor.feature.kendaraan.presentation.PilihKendaraanViewModel
import com.android.burdacontractor.feature.logistic.presentation.PilihLogisticViewModel
import com.android.burdacontractor.feature.perusahaan.presentation.PilihPerusahaanViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddDeliveryOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDeliveryOrderBinding
    private val addDeliveryOrderViewModel: AddDeliveryOrderViewModel by viewModels()
    private val storageViewModel: StorageViewModel by viewModels()
    private val pilihPerusahaanViewModel: PilihPerusahaanViewModel by viewModels()
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by viewModels()
    private val pilihGudangViewModel: PilihGudangViewModel by viewModels()
    private val pilihLogisticViewModel: PilihLogisticViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeliveryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snackbar = Snackbar.make(
            binding.mainLayout,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        addDeliveryOrderViewModel.liveNetworkChecker.observe(this) {
            checkConnection(snackbar, it) { initObserver() }
        }
    }

    private fun initObserver() {
        val sectionsPagerAdapter = AddDeliveryOrderPagerAdapter(this@AddDeliveryOrderActivity)
        binding.viewPager.adapter = sectionsPagerAdapter
        addDeliveryOrderViewModel.state.observe(this) {
            binding.progressBar.isVisible = it == StateResponse.LOADING
        }
        addDeliveryOrderViewModel.messageResponse.observe(this) {
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
                        addDeliveryOrderViewModel.perihal.observe(this) { perihal ->
                            addDeliveryOrderViewModel.tglPengambilan.observe(this) { tglPengambilan ->
                                addDeliveryOrderViewModel.untukPerhatian.observe(this) { untukPerhatian ->
                                    if (logistic != null &&
                                        kendaraan != null &&
                                        perusahaan != null &&
                                        gudang != null &&
                                        perihal != null &&
                                        tglPengambilan != null &&
                                        untukPerhatian != null
                                    ) {
                                        if (tglPengambilan.isNotBlank() && perihal.isNotBlank() && untukPerhatian.isNotBlank()) {
                                            addDeliveryOrderViewModel.setCanSwipe(true)
                                        }
                                    } else {
                                        addDeliveryOrderViewModel.setCanSwipe(false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        onBackPressedCallback()
        addDeliveryOrderViewModel.canSwipe.observe(this) {
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
                    addDeliveryOrderViewModel.addCreateStepOneDo(
                        logisticId = pilihLogisticViewModel.logistic.value!!.id,
                        purchasingId = storageViewModel.userId,
                        perusahaanId = pilihPerusahaanViewModel.perusahaan.value!!.id,
                        gudangId = pilihGudangViewModel.gudang.value!!.id,
                        kendaraanId = pilihKendaraanViewModel.kendaraan.value!!.id,
                        perihal = addDeliveryOrderViewModel.perihal.value!!,
                        tglPengambilan = addDeliveryOrderViewModel.tglPengambilan.value!!,
                        untukPerhatian = addDeliveryOrderViewModel.untukPerhatian.value!!
                    )
                }
                onBackPressedCallback()
            }
        })
    }

    fun onBackPressedCallback() {
        if (binding.viewPager.currentItem == 0) {
            val finishAction = {
                finish()
                overridePendingTransition(0, 0)
            }
            binding.btnBack.setOnClickListener {
                finishAction()
            }
            onBackPressedDispatcher { finishAction() }
            Log.d("currentItem == 0", binding.viewPager.currentItem.toString())

        } else if (binding.viewPager.currentItem == 1) {
            Log.d("currentItem == 1 ", binding.viewPager.currentItem.toString())
            binding.btnBack.setOnClickListener { binding.viewPager.currentItem = 0 }
            onBackPressedDispatcher { binding.viewPager.currentItem = 0 }
        }
    }

    private fun onBackPressedDispatcher(listener: () -> Unit) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener()
            }
        })
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.data_delivery_order,
            R.string.data_pre_order
        )
    }
}