package com.example.okok.ui.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import java.util.*

class HealthDataViewModel : ViewModel() {
    private val maxDataPoints = 50 // 最多显示50个数据点
    
    private val _heartRateData = MutableLiveData<List<Entry>>()
    val heartRateData: LiveData<List<Entry>> = _heartRateData
    
    private val entries = mutableListOf<Entry>()
    private var xValue = 0f

    private var timer: Timer? = null
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())

    fun addHeartRateData(value: Int) {
        if (entries.size > maxDataPoints) {
            entries.removeAt(0)
            // 更新所有点的x值
            entries.forEachIndexed { index, entry ->
                entry.x = index.toFloat()
            }
        }
        entries.add(Entry(xValue++, value.toFloat()))
        _heartRateData.value = entries.toList()
    }

    // 用于测试的模拟数据生成
    fun startGeneratingTestData() {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val randomHeartRate = (65..85).random()
                handler.post {
                    addHeartRateData(randomHeartRate)
                }
            }
        }, 0, 1000)
    }

    fun stopGeneratingTestData() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopGeneratingTestData()
    }
} 