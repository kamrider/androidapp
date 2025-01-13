package com.example.okok

import android.app.Application
import com.example.okok.bluetooth.BluetoothManager

class HealthMonitorApplication : Application() {
    lateinit var bluetoothManager: BluetoothManager
        private set
        
    override fun onCreate() {
        super.onCreate()
        bluetoothManager = BluetoothManager.getInstance(this)
    }
} 