package com.android.burdacontractor.feature.deliveryorder.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.burdacontractor.databinding.ActivityAddDeliveryOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDeliveryOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDeliveryOrderBinding
    private val addDeliveryOrderViewModel: AddDeliveryOrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeliveryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addDeliveryOrderViewModel.liveNetworkChecker.observe(this) {

        }
    }
}