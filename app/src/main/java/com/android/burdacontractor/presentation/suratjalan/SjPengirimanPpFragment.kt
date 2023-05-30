package com.android.burdacontractor.presentation.suratjalan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.burdacontractor.databinding.FragmentSjPengirimanPpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SjPengirimanPpFragment : Fragment() {
    private var _binding: FragmentSjPengirimanPpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSjPengirimanPpBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}