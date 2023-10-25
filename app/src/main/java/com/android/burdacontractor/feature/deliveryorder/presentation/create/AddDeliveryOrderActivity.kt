package com.android.burdacontractor.feature.deliveryorder.presentation.create

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
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
                        if (logistic != null && kendaraan != null && perusahaan != null && gudang != null) {
                            addDeliveryOrderViewModel.setCanSwipe(true)
                        }
                    }
                }
            }
        }
        addDeliveryOrderViewModel.canSwipe.observe(this) {
            binding.viewPager.isUserInputEnabled = it
            binding.tabs.isEnabled = it
            binding.tabs.touchables.forEach { view ->
                view.isEnabled = it
            }
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            val sectionsPagerAdapter = AddDeliveryOrderPagerAdapter(this@AddDeliveryOrderActivity)
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
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