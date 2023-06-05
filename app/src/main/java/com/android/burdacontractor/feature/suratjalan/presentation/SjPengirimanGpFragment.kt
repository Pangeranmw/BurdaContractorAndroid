package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.burdacontractor.R
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.presentation.adapter.ListSuratJalanAdapter
import com.android.burdacontractor.core.utils.checkConnection
import com.android.burdacontractor.databinding.FragmentSjPengirimanGpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SjPengirimanGpFragment : Fragment() {
    private var _binding: FragmentSjPengirimanGpBinding? = null
    private val binding get() = _binding!!
    private val suratJalanViewModel: SuratJalanViewModel by viewModels()
    private lateinit var adapter: ListSuratJalanAdapter

    private val tipe: SuratJalanTipe = SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK
    private var status: SuratJalanStatus = SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER
    private var dateStart: String? = null
    private var dateEnd: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSjPengirimanGpBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suratJalanViewModel.liveNetworkChecker.observe(viewLifecycleOwner){
            requireActivity().checkConnection(it,binding.root){
                initObserver()
            }
        }
    }
    private fun initObserver(){
        suratJalanViewModel.status.observe(viewLifecycleOwner){
            status = it
        }
        suratJalanViewModel.dateStart.observe(viewLifecycleOwner){
            dateStart = it
        }
        suratJalanViewModel.dateEnd.observe(viewLifecycleOwner){
            dateEnd = it
        }
        initUi()
    }
    private fun initUi(){
        setAdapter()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setAdapter(search = query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    private fun setAdapter(search: String? = null){
//        if(adapter!=null) adapter.refresh()
        adapter = ListSuratJalanAdapter{
            val mBundle = Bundle()
            mBundle.putString(SuratJalanFragment.SURAT_JALAN_ID, it.id)
            findNavController().navigate(R.id.action_surat_jalan_fragment_to_detailSuratJalanFragment, mBundle)
        }
        binding.rvSuratJalan.adapter = adapter
        suratJalanViewModel.getAllSuratJalan(
            tipe,
            status,
            dateStart,
            dateEnd,
            10,
            search,
        ).observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }
}