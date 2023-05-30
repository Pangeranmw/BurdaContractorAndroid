package com.android.burdacontractor.presentation.suratjalan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.burdacontractor.databinding.FragmentSjPengirimanGpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SjPengirimanGpFragment : Fragment() {
    private var _binding: FragmentSjPengirimanGpBinding? = null
    private val binding get() = _binding!!
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
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
    }
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}