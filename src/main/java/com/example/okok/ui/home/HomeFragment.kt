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
import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import com.example.okok.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.widget.Toast
import android.Manifest
import android.bluetooth.BluetoothDevice
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var connectionAnimation: LottieAnimationView
    private lateinit var heartRateTextView: TextView
    private lateinit var bloodOxygenTextView: TextView
    private lateinit var deviceListRecyclerView: RecyclerView
    private lateinit var noDevicesText: TextView
    private lateinit var refreshButton: MaterialButton
    private lateinit var bluetoothManager: BluetoothManager
    private var selectedDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = BluetoothManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        // 初始化视图引用
        connectionAnimation = binding.bluetoothAnimation
        heartRateTextView = binding.heartRate
        bloodOxygenTextView = binding.bloodOxygen
        
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
            // 处理设备选择
            selectedDevice = device
            // TODO: 显示连接按钮或直接开始连接
            showConnectDialog(device)
        }
        deviceListRecyclerView.adapter = deviceListAdapter
        
        refreshButton = binding.refreshButton
        
        // 添加刷新按钮点击事件
        refreshButton.setOnClickListener {
            startBluetoothScan()
        }
        
        setupBluetoothObservers()
        
        return binding.root
    }

    private fun setupBluetoothObservers() {
        bluetoothManager.isScanning.observe(viewLifecycleOwner) { scanning ->
            if (scanning) {
                connectionAnimation.playAnimation()
            } else {
                connectionAnimation.pauseAnimation()
                refreshButton.isEnabled = true
            }
        }

        bluetoothManager.discoveredDevices.observe(viewLifecycleOwner) { devices ->
            (deviceListRecyclerView.adapter as? DeviceListAdapter)?.updateDevices(devices)
            updateDeviceListVisibility(devices.isEmpty())
        }
    }

    private fun startBluetoothScan() {
        if (!checkAndRequestPermissions()) {
            return
        }
        // 开始旋转动画
        refreshButton.isEnabled = false
        val rotation = ObjectAnimator.ofFloat(refreshButton, View.ROTATION, 0f, 360f)
        rotation.duration = 1000
        rotation.interpolator = LinearInterpolator()
        rotation.start()
        
        // 更新UI状态
        deviceListRecyclerView.visibility = View.GONE
        noDevicesText.visibility = View.VISIBLE
        noDevicesText.text = getString(R.string.scanning_devices)
        
        // 开始扫描
        bluetoothManager.startScan()
    }

    private fun updateDeviceListVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            noDevicesText.text = getString(R.string.no_devices_found)
            noDevicesText.visibility = View.VISIBLE
            deviceListRecyclerView.visibility = View.GONE
        } else {
            noDevicesText.visibility = View.GONE
            deviceListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        
        val permissionsToRequest = permissions.filter {
            ActivityCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 权限已授予，可以开始扫描
                startBluetoothScan()
            } else {
                // 显示权限被拒绝的提示
                Toast.makeText(context, "需要蓝牙和位置权限才能扫描设备", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConnectDialog(device: BluetoothDevice) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("连接设备")
            .setMessage("是否连接到设备：${device.name ?: "未知设备"}?")
            .setPositiveButton("连接") { _, _ ->
                // TODO: 实现连接逻辑
            }
            .setNegativeButton("取消", null)
            .show()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}