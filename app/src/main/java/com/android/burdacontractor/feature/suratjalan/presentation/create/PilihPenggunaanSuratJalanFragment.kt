package com.android.burdacontractor.feature.suratjalan.presentation.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.presentation.StorageViewModel
import com.android.burdacontractor.core.presentation.adapter.ListPenggunaanSuratJalanAdapter
import com.android.burdacontractor.core.utils.setGone
import com.android.burdacontractor.core.utils.setVisible
import com.android.burdacontractor.databinding.FragmentPilihPenggunaanBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PilihPenggunaanSuratJalanFragment : DialogFragment() {
    private var _binding: FragmentPilihPenggunaanBinding? = null
    private val pilihPenggunaanSuratJalanViewModel: PilihPenggunaanSuratJalanViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private lateinit var adapter: ListPenggunaanSuratJalanAdapter
    private var tipe: String? = null
    private var proyekId: String? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihPenggunaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tipe?.let {
            pilihPenggunaanSuratJalanViewModel.setTipe(it)
        }
        proyekId?.let {
            pilihPenggunaanSuratJalanViewModel.setProyekId(it)
        }
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
        with(pilihPenggunaanSuratJalanViewModel) {
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
            initUi()
        }
    }

    private fun initUi() {
        with(binding) {
            srLayout.setOnRefreshListener {
                setAdapter()
            }
            val gridLayoutManager = GridLayoutManager(
                requireContext(), 1,
                GridLayoutManager.VERTICAL, false
            )
            binding.rvPenggunaan.layoutManager = gridLayoutManager
            binding.rvPenggunaan.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    srLayout.isEnabled =
                        gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
                }
            })
            binding.btnClose.setOnClickListener {
                dismiss()
            }
            adapter = ListPenggunaanSuratJalanAdapter(
                checkedVisible = true,
                checkedListData = pilihPenggunaanSuratJalanViewModel.selectedListPenggunaan.value!!,
                checkedDataListener = { penggunaan, b ->
                    if (b) {
                        pilihPenggunaanSuratJalanViewModel.addPenggunaan(penggunaan)
                    } else {
                        pilihPenggunaanSuratJalanViewModel.removePenggunaan(penggunaan)
                    }
                },
                userId = storageViewModel.userId
            )
            rvPenggunaan.adapter = adapter
            setAdapter()
        }
    }

    private fun setAdapter() {
        pilihPenggunaanSuratJalanViewModel.getAvailablePenggunaanByProyek()
        pilihPenggunaanSuratJalanViewModel.listPenggunaan.observe(this) {
            if (it.isEmpty()) binding.tvEmptyPenggunaan.setVisible()
            else binding.tvEmptyPenggunaan.setGone()
            adapter.submitList(it)
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
        val TAG = PilihPenggunaanSuratJalanFragment::class.java.simpleName
        fun newInstance(tipe: String, proyekId: String): PilihPenggunaanSuratJalanFragment {
            val fragment = PilihPenggunaanSuratJalanFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.tipe = tipe
            fragment.proyekId = proyekId
            return fragment
        }
    }
}