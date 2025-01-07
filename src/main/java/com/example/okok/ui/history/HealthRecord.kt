package com.example.okok.ui.history

data class HealthRecord(
    val timestamp: Long,
    val heartRate: Int,
    val bloodOxygen: Int,
    val date: String // 格式化后的日期字符串
) 