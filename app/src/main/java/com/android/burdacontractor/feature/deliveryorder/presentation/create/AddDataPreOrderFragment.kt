package com.android.burdacontractor.feature.deliveryorder.presentation.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.databinding.FragmentAddDataDeliveryOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDataPreOrderFragment : Fragment() {
    private var _binding: FragmentAddDataDeliveryOrderBinding? = null
    private val addDeliveryOrderViewModel: AddDeliveryOrderViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private var index: Int? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDataDeliveryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}