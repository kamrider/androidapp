package com.example.okok.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.example.okok.databinding.FragmentHomeBinding
import com.example.okok.R
import android.bluetooth.BluetoothDevice
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var connectionAnimation: LottieAnimationView
    private lateinit var heartRateTextView: TextView
    private lateinit var bloodOxygenTextView: TextView
    private lateinit var startMonitoringButton: MaterialButton
    private lateinit var connectButton: MaterialButton
    private lateinit var deviceListRecyclerView: RecyclerView
    private lateinit var noDevicesText: TextView
    private var selectedDevice: BluetoothDevice? = null
    private lateinit var refreshButton: MaterialButton

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
        startMonitoringButton = binding.startMonitoring
        connectButton = binding.connectButton
        
        // 设置动画
        connectionAnimation.setAnimation(R.raw.bluetooth_scanning)
        connectionAnimation.playAnimation()
        
        // 设置初始值
        heartRateTextView.text = getString(R.string.default_heart_rate)
        bloodOxygenTextView.text = getString(R.string.default_blood_oxygen)
        
        deviceListRecyclerView = binding.deviceList
        noDevicesText = binding.noDevicesText
        
        // 设置RecyclerView
        deviceListRecyclerView.layoutManager = LinearLayoutManager(context)
        val deviceListAdapter = DeviceListAdapter(emptyList()) { device ->
            selectedDevice = device
            connectButton.isEnabled = true
        }
        deviceListRecyclerView.adapter = deviceListAdapter
        
        // 修改连接按钮点击事件
        connectButton.isEnabled = false // 初始状态禁用
        connectButton.setOnClickListener {
            selectedDevice?.let { device ->
                connectButton.isEnabled = false
                connectButton.text = getString(R.string.connecting)
                // TODO: 实现与选中设备的连接逻辑
            }
        }
        
        refreshButton = binding.refreshButton
        
        // 添加刷新按钮点击事件
        refreshButton.setOnClickListener {
            // 开始旋转动画
            refreshButton.isEnabled = false
            val rotation = ObjectAnimator.ofFloat(refreshButton, View.ROTATION, 0f, 360f)
            rotation.duration = 1000
            rotation.interpolator = LinearInterpolator()
            rotation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    refreshButton.isEnabled = true
                    // 动画结束后，如果没有找到设备，显示"无设备"
                    if (deviceListRecyclerView.adapter?.itemCount == 0) {
                        noDevicesText.text = getString(R.string.no_devices_found)
                        noDevicesText.visibility = View.VISIBLE
                    } else {
                        noDevicesText.visibility = View.GONE
                    }
                }
            })
            rotation.start()
            
            // 显示搜索状态
            deviceListRecyclerView.visibility = View.GONE
            noDevicesText.visibility = View.VISIBLE
            noDevicesText.text = getString(R.string.scanning_devices)
            
            // TODO: 实现蓝牙设备扫描逻辑
        }
        
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}