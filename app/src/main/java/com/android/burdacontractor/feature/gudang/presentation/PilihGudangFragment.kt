package com.android.burdacontractor.feature.gudang.presentation

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.FilterSelected
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListFilterSelectedAdapter
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListGudangAdapter
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentPilihGudangBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PilihGudangFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPilihGudangBinding? = null
    private val pilihGudangViewModel: PilihGudangViewModel by activityViewModels()
    private val gudangViewModel: GudangViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var filterDialog: FilterGudangFragment
    private lateinit var adapter: PagingListGudangAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihGudangBinding.inflate(inflater, container, false)
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
        with(gudangViewModel) {
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
            filterDialog = FilterGudangFragment.newInstance()
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    gudangViewModel.setSearch(searchView.text.toString())
                    setAdapter()
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            binding.rvGudang.layoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListGudangAdapter { gudang ->
                pilihGudangViewModel.setGudang(gudang)
                dismiss()
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyGudang.setVisible()
                            } else {
                                binding.tvEmptyGudang.setGone()
                            }
                            gudangViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        gudangViewModel.setState(StateResponse.LOADING)
                    }

                    is LoadState.Error -> {
                        gudangViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvGudang.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
            btnFilter.setOnClickListener {
                filterDialog.setOnClickListener(object :
                    FilterGudangFragment.OnClickListener {
                    override fun onClickListener() {
                        setAdapter()
                    }
                })
                filterDialog.show(requireActivity().supportFragmentManager)
            }
        }
    }

    private fun setAdapter() {
        gudangViewModel.getAllGudang().observe(this) {
            adapter.submitData(lifecycle, it)
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
        val TAG = PilihGudangFragment::class.java.simpleName
        fun newInstance(): PilihGudangFragment {
            val fragment = PilihGudangFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}