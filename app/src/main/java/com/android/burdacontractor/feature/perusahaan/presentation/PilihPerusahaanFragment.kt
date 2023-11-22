package com.android.burdacontractor.feature.perusahaan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListPerusahaanAdapter
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentPilihPerusahaanBinding
import com.android.burdacontractor.feature.perusahaan.presentation.main.PerusahaanViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PilihPerusahaanFragment : DialogFragment() {
    private var _binding: FragmentPilihPerusahaanBinding? = null
    private val pilihPerusahaanViewModel: PilihPerusahaanViewModel by activityViewModels()
    private val perusahaanViewModel: PerusahaanViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var filterDialog: FilterPerusahaanFragment
    private lateinit var adapter: PagingListPerusahaanAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihPerusahaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun initObserver() {
        with(perusahaanViewModel) {
            state.observe(viewLifecycleOwner) {
                binding.srLayout.isRefreshing = it == StateResponse.LOADING
            }
            setCoordinate(
                getDistanceMatrixCoordinate(
                    storageViewModel.latitude,
                    storageViewModel.longitude
                )
            )
            messageResponse.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { messageResponse ->
                    Snackbar.make(
                        binding.root,
                        messageResponse,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            provinsiIndex.observe(viewLifecycleOwner) { provinsiIndex ->
                if (provinsiIndex != null) {
                    val listFilter = mutableListOf<FilterSelected>()
                    val filterAdapter = ListFilterSelectedAdapter {
                        if (it.index == 0) setProvinsiIndex(null)
                        listFilter.remove(it)
                        setAdapter()
                    }
                    listFilter.add(FilterSelected(0, listProvinsi.value!![provinsiIndex]))
                    binding.rvFilter.setVisible()
                    filterAdapter.submitList(listFilter)
                    binding.rvFilter.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.rvFilter.adapter = filterAdapter
                } else {
                    binding.rvFilter.setGone()
                }
            }
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            filterDialog = FilterPerusahaanFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    perusahaanViewModel.setSearch(searchView.text.toString())
                    setAdapter()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            val gridLayoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            binding.rvPerusahaan.layoutManager = gridLayoutManager
            adapter = PagingListPerusahaanAdapter { perusahaan ->
                pilihPerusahaanViewModel.setPerusahaan(perusahaan)
                dismiss()
            }
            binding.rvPerusahaan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    srLayout.isEnabled =
                        gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
                }
            })
            binding.btnClose.setOnClickListener {
                dismiss()
            }
            adapter.addLoadStateListener { loadState ->
                if ((loadState.refresh is LoadState.Loading) || (loadState.append is LoadState.Loading)) {
                    perusahaanViewModel.setState(StateResponse.LOADING)
                } else if ((loadState.append is LoadState.NotLoading) && (loadState.refresh is LoadState.NotLoading)) {
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                        binding.tvEmptyPerusahaan.setVisible()
                    } else {
                        binding.tvEmptyPerusahaan.setGone()
                    }
                    perusahaanViewModel.setState(StateResponse.SUCCESS)
                } else if ((loadState.refresh is LoadState.Error) || (loadState.append is LoadState.Error)) {
                    perusahaanViewModel.setState(StateResponse.ERROR)
                }
            }
            rvPerusahaan.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterPerusahaanFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(requireActivity().supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        perusahaanViewModel.getAllPerusahaan().observe(this) {
            adapter.submitData(lifecycle, it)
        }
        perusahaanViewModel.getPerusahaanProvinsi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    companion object {
        val TAG = PilihPerusahaanFragment::class.java.simpleName
        fun newInstance(): PilihPerusahaanFragment {
            val fragment = PilihPerusahaanFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}