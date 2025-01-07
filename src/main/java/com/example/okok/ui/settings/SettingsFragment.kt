package com.example.okok.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.okok.databinding.FragmentSettingsBinding
import com.google.android.material.slider.Slider

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupSliders()
        setupSwitches()
        setupSaveButton()
        observeViewModel()

        return binding.root
    }

    private fun setupSliders() {
        binding.heartRateThreshold.addOnChangeListener { _, value, _ ->
            binding.heartRateValue.text = "${value.toInt()} BPM"
            viewModel.setHeartRateThreshold(value.toInt())
        }

        binding.bloodOxygenThreshold.addOnChangeListener { _, value, _ ->
            binding.bloodOxygenValue.text = "${value.toInt()}%"
            viewModel.setBloodOxygenThreshold(value.toInt())
        }
    }

    private fun setupSwitches() {
        binding.enableAlerts.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAlertsEnabled(isChecked)
        }

        binding.cloudSync.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCloudSyncEnabled(isChecked)
        }
    }

    private fun setupSaveButton() {
        binding.saveSettings.setOnClickListener {
            viewModel.saveSettings()
            // TODO: 显示保存成功提示
        }
    }

    private fun observeViewModel() {
        viewModel.heartRateThreshold.observe(viewLifecycleOwner) { threshold ->
            binding.heartRateThreshold.value = threshold.toFloat()
            binding.heartRateValue.text = "$threshold BPM"
        }

        viewModel.bloodOxygenThreshold.observe(viewLifecycleOwner) { threshold ->
            binding.bloodOxygenThreshold.value = threshold.toFloat()
            binding.bloodOxygenValue.text = "$threshold%"
        }

        viewModel.alertsEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.enableAlerts.isChecked = enabled
        }

        viewModel.cloudSyncEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.cloudSync.isChecked = enabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 