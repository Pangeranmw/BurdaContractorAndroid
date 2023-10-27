package com.android.burdacontractor.feature.kendaraan.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListKendaraanAdapter
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setToastLong
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentPilihKendaraanBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PilihKendaraanFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPilihKendaraanBinding? = null
    private val pilihKendaraanViewModel: PilihKendaraanViewModel by activityViewModels()
    private val kendaraanViewModel: KendaraanViewModel by activityViewModels()
    private lateinit var filterDialog: FilterKendaraanFragment
    private lateinit var adapter: PagingListKendaraanAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihKendaraanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bsd = dialog as BottomSheetDialog
            val bottomSheet =
                bsd.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.semi_rounded_top_white)
            // FullScreen
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet.layoutParams = layoutParams
        }
        return bottomSheetDialog
    }

    private fun initObserver() {
        with(kendaraanViewModel) {
            state.observe(viewLifecycleOwner) {
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            messageResponse.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            statusIndex.observe(viewLifecycleOwner) { statusIndex ->
                jenisIndex.observe(viewLifecycleOwner) { jenisIndex ->
                    gudangIndex.observe(viewLifecycleOwner) { gudangIndex ->
                        val listFilter = mutableListOf<FilterSelected>()
                        val filterAdapter = ListFilterSelectedAdapter {
                            if (it.index == 0) setStatusIndex(null)
                            if (it.index == 1) setJenisIndex(null)
                            if (it.index == 2) setGudangIndex(null)
                            listFilter.remove(it)
                            setAdapter()
                        }
                        if (statusIndex != null) {
                            listFilter.add(
                                FilterSelected(
                                    0,
                                    enumValueToNormal(listStatus.value!![statusIndex])
                                )
                            )
                        }
                        if (jenisIndex != null) {
                            listFilter.add(
                                FilterSelected(
                                    1,
                                    enumValueToNormal(listJenis.value!![jenisIndex])
                                )
                            )
                        }
                        if (gudangIndex != null) {
                            listFilter.add(FilterSelected(2, listGudang.value!![gudangIndex].nama))
                        }
                        if (listFilter.isNotEmpty()) binding.rvFilter.setVisible()
                        else binding.rvFilter.setGone()
                        filterAdapter.submitList(listFilter)
                        binding.rvFilter.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        binding.rvFilter.adapter = filterAdapter
                    }
                }
            }
            initUi()
        }
    }

    private fun initUi() {
        with(binding) {
            filterDialog = FilterKendaraanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    kendaraanViewModel.setSearch(searchView.text.toString())
                    setAdapter()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            binding.rvKendaraan.layoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListKendaraanAdapter { kendaraan ->
                if (kendaraan.logisticId == null) {
                    pilihKendaraanViewModel.setKendaraan(kendaraan)
                    dismiss()
                } else {
                    requireActivity().setToastLong("Kendaraan Sedang Digunakan, Silahkan Pilih yang Lain")
                }
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyKendaraan.setVisible()
                            } else {
                                binding.tvEmptyKendaraan.setGone()
                            }
                            kendaraanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        if (adapter.itemCount == 0) {
                            kendaraanViewModel.setState(StateResponse.LOADING)
                        } else {
                            kendaraanViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Error -> {
                        kendaraanViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvKendaraan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterKendaraanFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(requireActivity().supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        kendaraanViewModel.getAllKendaraan().observe(this) {
            val onlyAvailableKendaraan = it.filter { paging -> paging.logisticId == null }
            adapter.submitData(lifecycle, onlyAvailableKendaraan)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {
        val TAG = PilihKendaraanFragment::class.java.simpleName
        fun newInstance(): PilihKendaraanFragment {
            val fragment = PilihKendaraanFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}