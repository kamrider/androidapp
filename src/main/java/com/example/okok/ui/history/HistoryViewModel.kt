package com.example.okok.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel : ViewModel() {
    private val _healthRecords = MutableLiveData<List<HealthRecord>>()
    val healthRecords: LiveData<List<HealthRecord>> = _healthRecords
    
    private var startDate: Long = 0
    private var endDate: Long = System.currentTimeMillis()
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    init {
        // 初始化时加载最近7天的数据
        startDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        loadHealthRecords()
    }
    
    fun setStartDate(date: Long) {
        startDate = date
        loadHealthRecords()
    }
    
    fun setEndDate(date: Long) {
        endDate = date
        loadHealthRecords()
    }
    
    private fun loadHealthRecords() {
        // TODO: 从数据库或其他数据源加载数据
        // 临时使用模拟数据
        val records = mutableListOf<HealthRecord>()
        var currentTime = startDate
        while (currentTime <= endDate) {
            records.add(
                HealthRecord(
                    timestamp = currentTime,
                    heartRate = (65..85).random(),
                    bloodOxygen = (95..99).random(),
                    date = dateFormat.format(Date(currentTime))
                )
            )
            currentTime += 30 * 60 * 1000 // 每30分钟一条记录
        }
        _healthRecords.value = records
    }
} 