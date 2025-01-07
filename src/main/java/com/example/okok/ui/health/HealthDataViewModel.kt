package com.example.okok.ui.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HealthDataViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是健康数据页面"
    }
    val text: LiveData<String> = _text
} 