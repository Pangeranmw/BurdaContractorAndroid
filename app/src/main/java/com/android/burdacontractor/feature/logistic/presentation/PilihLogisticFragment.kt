package com.android.burdacontractor.feature.logistic.presentation

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
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.LoadingStateAdapter
import com.android.burdacontractor.core.presentation.adapter.PagingListLogisticAdapter
import com.android.burdacontractor.core.utils.getDistanceMatrixCoordinate
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentPilihLogisticBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PilihLogisticFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentPilihLogisticBinding? = null
    private val pilihLogisticViewModel: PilihLogisticViewModel by activityViewModels()
    private val logisticViewModel: LogisticViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var adapter: PagingListLogisticAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihLogisticBinding.inflate(inflater, container, false)
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
        with(logisticViewModel) {
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
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    logisticViewModel.setSearch(searchView.text.toString())
                    setAdapter(true)
                    searchView.hide()
                    false
                }
            srLayout.setOnRefreshListener {
                setAdapter(true)
            }
            binding.rvLogistic.layoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            adapter = PagingListLogisticAdapter { logistic ->
                pilihLogisticViewModel.setLogistic(logistic)
                dismiss()
            }
            adapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> {
                        if (loadState.source.refresh is LoadState.NotLoading) {
                            if (loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.tvEmptyLogistic.setVisible()
                            } else {
                                binding.tvEmptyLogistic.setGone()
                            }
                            logisticViewModel.setState(StateResponse.SUCCESS)
                        }
                    }

                    is LoadState.Loading -> {
                        logisticViewModel.setState(StateResponse.LOADING)
                    }

                    is LoadState.Error -> {
                        logisticViewModel.setState(StateResponse.ERROR)
                    }
                }
            }
            rvLogistic.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            setAdapter()
        }
    }

    private fun setAdapter(isRefresh: Boolean = false) {
        if (isRefresh) {
            adapter.submitData(lifecycle, PagingData.empty())
        }
        logisticViewModel.getAllLogistic().observe(this) {
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
        val TAG = PilihLogisticFragment::class.java.simpleName
        fun newInstance(): PilihLogisticFragment {
            val fragment = PilihLogisticFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}