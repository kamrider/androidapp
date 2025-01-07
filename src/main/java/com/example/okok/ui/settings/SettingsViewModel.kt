package com.example.okok.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _heartRateThreshold = MutableLiveData<Int>().apply { value = 120 }
    val heartRateThreshold: LiveData<Int> = _heartRateThreshold

    private val _bloodOxygenThreshold = MutableLiveData<Int>().apply { value = 95 }
    val bloodOxygenThreshold: LiveData<Int> = _bloodOxygenThreshold

    private val _alertsEnabled = MutableLiveData<Boolean>().apply { value = true }
    val alertsEnabled: LiveData<Boolean> = _alertsEnabled

    private val _cloudSyncEnabled = MutableLiveData<Boolean>().apply { value = false }
    val cloudSyncEnabled: LiveData<Boolean> = _cloudSyncEnabled

    fun setHeartRateThreshold(value: Int) {
        _heartRateThreshold.value = value
    }

    fun setBloodOxygenThreshold(value: Int) {
        _bloodOxygenThreshold.value = value
    }

    fun setAlertsEnabled(enabled: Boolean) {
        _alertsEnabled.value = enabled
    }

    fun setCloudSyncEnabled(enabled: Boolean) {
        _cloudSyncEnabled.value = enabled
    }

    fun saveSettings() {
        // TODO: 保存设置到 SharedPreferences 或其他持久化存储
    }
} 