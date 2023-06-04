package com.android.burdacontractor.feature.suratjalan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.burdacontractor.R
import com.android.burdacontractor.databinding.FragmentSuratJalanBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuratJalanFragment : Fragment() {
    private var _binding: FragmentSuratJalanBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuratJalanBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionsPagerAdapter = SuratJalanPagerAdapter(childFragmentManager, lifecycle)
        sectionsPagerAdapter.populateFragment(SjPengirimanGpFragment(), resources.getString(R.string.surat_jalan_gp))
        sectionsPagerAdapter.populateFragment(SjPengirimanPpFragment(), resources.getString(R.string.surat_jalan_pp))
        sectionsPagerAdapter.populateFragment(SjPengembalianFragment(), resources.getString(R.string.surat_jalan_pengembalian))

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
        }.attach()
    }
    companion object {
        val SURAT_JALAN_ID = "surat_jalan_id"
    }
}