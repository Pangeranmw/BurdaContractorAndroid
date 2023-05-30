package com.android.burdacontractor.presentation.suratjalan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.burdacontractor.databinding.FragmentSjPengembalianBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SjPengembalianFragment : Fragment() {
    private var _binding: FragmentSjPengembalianBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSjPengembalianBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}