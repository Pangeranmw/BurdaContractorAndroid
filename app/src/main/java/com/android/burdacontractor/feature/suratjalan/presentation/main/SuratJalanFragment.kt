package com.android.burdacontractor.feature.suratjalan.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.Constant.ARG_SECTION_NUMBER
import com.android.burdacontractor.core.domain.model.Constant.INTENT_ID
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListSuratJalanAdapter
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.openActivityWithExtras
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentSuratJalanBinding
import com.android.burdacontractor.feature.suratjalan.presentation.detail.SuratJalanDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SuratJalanFragment : Fragment() {
    private var _binding: FragmentSuratJalanBinding? = null
    private val suratJalanViewModel: SuratJalanViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var adapter: PagingListSuratJalanAdapter
    private var index: Int? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuratJalanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        binding.rvSj.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = PagingListSuratJalanAdapter(
            storageViewModel.role,
            storageViewModel.userId
        ) { suratJalan ->
            requireActivity().openActivityWithExtras(SuratJalanDetailActivity::class.java, false) {
                putString(INTENT_ID, suratJalan.id)
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                if ((it.refresh is LoadState.Loading) || (it.append is LoadState.Loading)) {
                    suratJalanViewModel.setState(StateResponse.LOADING)
                } else if ((it.refresh is LoadState.NotLoading) || (it.append is LoadState.NotLoading)) {
                    suratJalanViewModel.setState(StateResponse.SUCCESS)
                } else if ((it.refresh is LoadState.Error) || (it.append is LoadState.Error)) {
                    suratJalanViewModel.setState(StateResponse.ERROR)
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.NotLoading -> {
                    if (loadState.source.refresh is LoadState.NotLoading) {
                        if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                            binding.tvEmptySuratJalan.setVisible()
                            binding.tvCountSj.text = "0"
                            binding.tvCountSj.setBackgroundResource(R.drawable.semi_rounded_red)
                        } else {
                            binding.tvEmptySuratJalan.setGone()
                            binding.tvCountSj.text =
                                adapter.snapshot().items[0].totalData.toString()
                            binding.tvCountSj.setBackgroundResource(R.drawable.semi_rounded_secondary_main)
                        }
                        suratJalanViewModel.setState(StateResponse.SUCCESS)
                    }
                }
                is LoadState.Loading -> {
                    if (adapter.itemCount == 0) {
                        suratJalanViewModel.setState(StateResponse.LOADING)
                    } else {
                        suratJalanViewModel.setState(StateResponse.SUCCESS)
                    }
                }
                is LoadState.Error -> {
                    suratJalanViewModel.setState(StateResponse.ERROR)
                }
            }
        }

        binding.rvSj.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        when (index) {
            1 -> setAdapter(1)
            2 -> setAdapter(2)
            3 -> setAdapter(3)
            4 -> setAdapter(4)
        }
    }

    override fun onResume() {
        super.onResume()
        index?.let {
            setAdapter(it, true)
        }
    }

    fun setAdapter(index: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            adapter.submitData(lifecycle, PagingData.empty())
        }
        when (index) {
            1 -> suratJalanViewModel.setStatus(SuratJalanStatus.SEMUA)
            2 -> suratJalanViewModel.setStatus(SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER)
            3 -> suratJalanViewModel.setStatus(SuratJalanStatus.DRIVER_DALAM_PERJALANAN)
            4 -> suratJalanViewModel.setStatus(SuratJalanStatus.SELESAI)
        }
        binding.tvHeadingStatus.text =
            if (suratJalanViewModel.status.value!! != SuratJalanStatus.SEMUA) getString(
                R.string.heading_sj_pager,
                enumValueToNormal(suratJalanViewModel.tipe.value!!.name),
                enumValueToNormal(suratJalanViewModel.status.value!!.name),
            ) else getString(
                R.string.heading_sj_pager_all,
                enumValueToNormal(suratJalanViewModel.tipe.value!!.name)
            )
        suratJalanViewModel.getAllSuratJalan().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}