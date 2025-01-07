package com.example.okok.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.okok.databinding.FragmentHomeBinding
import com.example.okok.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var connectionAnimation: LottieAnimationView
    private lateinit var heartRateTextView: TextView
    private lateinit var bloodOxygenTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        // 初始化视图引用，使用正确的ID
        connectionAnimation = binding.bluetoothAnimation
        heartRateTextView = binding.heartRate
        bloodOxygenTextView = binding.bloodOxygen
        
        // 设置动画
        connectionAnimation.setAnimation(R.raw.bluetooth_scanning)
        connectionAnimation.playAnimation()
        
        // 设置初始值
        heartRateTextView.text = getString(R.string.default_heart_rate)
        bloodOxygenTextView.text = getString(R.string.default_blood_oxygen)
        
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}