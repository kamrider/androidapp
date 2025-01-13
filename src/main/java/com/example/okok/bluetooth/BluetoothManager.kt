package com.example.okok.bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BluetoothManager private constructor(private val context: Context) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private var bluetoothGatt: BluetoothGatt? = null
    
    // LiveData 用于观察状态变化
    private val _isScanning = MutableLiveData<Boolean>()
    val isScanning: LiveData<Boolean> = _isScanning
    
    private val _discoveredDevices = MutableLiveData<MutableList<BluetoothDevice>>()
    val discoveredDevices: LiveData<MutableList<BluetoothDevice>> = _discoveredDevices

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    // 添加已连接设备的引用
    private var _connectedDevice = MutableLiveData<BluetoothDevice?>()
    val connectedDevice: LiveData<BluetoothDevice?> = _connectedDevice

    companion object {
        private const val SCAN_PERIOD = 10000L // 扫描持续时间：10秒
        
        @Volatile
        private var instance: BluetoothManager? = null
        
        fun getInstance(context: Context): BluetoothManager {
            return instance ?: synchronized(this) {
                instance ?: BluetoothManager(context.applicationContext).also { 
                    instance = it 
                }
            }
        }
    }

    // 扫描回调
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val currentDevices = _discoveredDevices.value ?: mutableListOf()
            if (!currentDevices.contains(device)) {
                currentDevices.add(device)
                _discoveredDevices.postValue(currentDevices)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            _isScanning.postValue(false)
            scanning = false
        }
    }

    // 开始扫描
    fun startScan() {
        if (bluetoothAdapter?.isEnabled == true && !scanning) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
            _isScanning.value = true
            scanning = true
            _discoveredDevices.value = mutableListOf()
            
            try {
                bluetoothLeScanner?.startScan(scanCallback)
                // 10秒后自动停止扫描
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    stopScan()
                }, SCAN_PERIOD)
            } catch (e: SecurityException) {
                _isScanning.value = false
                scanning = false
            }
        }
    }

    // 停止扫描
    fun stopScan() {
        if (scanning && bluetoothAdapter?.isEnabled == true) {
            try {
                bluetoothLeScanner?.stopScan(scanCallback)
            } catch (e: SecurityException) {
                // 处理权限异常
            } finally {
                scanning = false
                _isScanning.value = false
            }
        }
    }

    // GATT 回调
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    _isConnected.postValue(true)
                    _connectedDevice.postValue(gatt?.device)
                    bluetoothGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _isConnected.postValue(false)
                    _connectedDevice.postValue(null)
                    bluetoothGatt?.close()
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // TODO: 处理服务发现成功
            }
        }
    }

    // 连接设备
    fun connect(device: BluetoothDevice) {
        try {
            bluetoothGatt = device.connectGatt(context, false, gattCallback)
        } catch (e: SecurityException) {
            _isConnected.postValue(false)
        }
    }

    // 断开连接
    fun disconnect() {
        try {
            bluetoothGatt?.disconnect()
        } catch (e: SecurityException) {
            // 处理权限异常
        }
    }
}