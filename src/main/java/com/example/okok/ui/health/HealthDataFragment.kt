package com.example.okok.ui.health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.okok.databinding.FragmentHealthDataBinding

class HealthDataFragment : Fragment() {

    private var _binding: FragmentHealthDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val healthDataViewModel =
            ViewModelProvider(this).get(HealthDataViewModel::class.java)

        _binding = FragmentHealthDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHealthData
        healthDataViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 