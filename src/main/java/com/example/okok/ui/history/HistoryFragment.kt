package com.example.okok.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.okok.databinding.FragmentHistoryBinding
import com.example.okok.ui.history.HealthRecordAdapter
import java.util.*

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HealthRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupDatePickers()
        observeData()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = HealthRecordAdapter(emptyList()) { record ->
            // TODO: 处理记录点击事件，显示详细信息
        }
        binding.historyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HistoryFragment.adapter
        }
    }

    private fun setupDatePickers() {
        binding.btnStartDate.setOnClickListener {
            showDatePicker(true)
        }
        binding.btnEndDate.setOnClickListener {
            showDatePicker(false)
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = Calendar.getInstance().apply {
                    set(year, month, day)
                }.timeInMillis
                if (isStartDate) {
                    viewModel.setStartDate(date)
                } else {
                    viewModel.setEndDate(date)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeData() {
        viewModel.healthRecords.observe(viewLifecycleOwner) { records ->
            adapter.updateData(records)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 