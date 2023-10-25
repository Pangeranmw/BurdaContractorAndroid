package com.android.burdacontractor.feature.deliveryorder.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.ARG_SECTION_NUMBER
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListDOAdapter
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentDeliveryOrderBinding
import com.android.burdacontractor.feature.deliveryorder.presentation.detail.DeliveryOrderDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryOrderFragment : Fragment() {
    private var _binding: FragmentDeliveryOrderBinding? = null
    private val deliveryOrderViewModel: DeliveryOrderViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var adapter: PagingListDOAdapter
    private var index: Int? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeliveryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        binding.rvDo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = PagingListDOAdapter(storageViewModel.role, storageViewModel.userId) { deliveryOrder ->
                requireActivity().openActivityWithExtras(DeliveryOrderDetailActivity::class.java, false) {
                    putString(INTENT_ID, deliveryOrder.id)
                }
            }
        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.NotLoading -> {
                    if (loadState.source.refresh is LoadState.NotLoading) {
                        if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                            binding.tvEmptyDeliveryOrder.setVisible()
                            binding.tvCountDo.text = "0"
                            binding.tvCountDo.setBackgroundResource(R.drawable.semi_rounded_red)
                        }else{
                            binding.tvEmptyDeliveryOrder.setGone()
                            binding.tvCountDo.text = adapter.snapshot().items[0].totalData.toString()
                            binding.tvCountDo.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                        }
                        deliveryOrderViewModel.setState(StateResponse.SUCCESS)
                    }
                }
                is LoadState.Loading -> {
                    if (adapter.itemCount == 0) {
                        deliveryOrderViewModel.setState(StateResponse.LOADING)
                    } else {
                        deliveryOrderViewModel.setState(StateResponse.SUCCESS)
                    }
                }
                is LoadState.Error -> {
                    deliveryOrderViewModel.setState(StateResponse.ERROR)
                }
            }
        }

        binding.rvDo.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        when (index) {
            1 -> setAdapter(1)
            2 -> setAdapter(2)
            3 -> setAdapter(3)
        }
    }

    override fun onResume() {
        super.onResume()
        index?.let{
            setAdapter(it, true)
        }
    }
    fun setAdapter(index: Int, isRefresh: Boolean = false){
        if(isRefresh) { adapter.submitData(lifecycle,PagingData.empty()) }
        when (index) {
            1 -> {
                deliveryOrderViewModel.setStatus(DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER)
                binding.tvHeadingStatus.text =
                    getString(R.string.do_menunggu_konfirmasi_driver)
            }
            2 -> {
                deliveryOrderViewModel.setStatus(DeliveryOrderStatus.DRIVER_DALAM_PERJALANAN)
                binding.tvHeadingStatus.text =
                    getString(R.string.do_driver_dalam_perjalanan)
            }
            3 -> {
                deliveryOrderViewModel.setStatus(DeliveryOrderStatus.SELESAI)
                binding.tvHeadingStatus.text =
                    getString(R.string.do_selesai)
            }
        }
        deliveryOrderViewModel.getAllDelivery().observe(viewLifecycleOwner){
            adapter.submitData(lifecycle,it)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}