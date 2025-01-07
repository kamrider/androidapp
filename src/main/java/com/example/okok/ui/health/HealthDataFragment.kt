package com.example.okok.ui.health

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.okok.databinding.FragmentHealthDataBinding
import com.github.mikephil.charting.formatter.ValueFormatter

class HealthDataFragment : Fragment() {
    private var _binding: FragmentHealthDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart
    private lateinit var viewModel: HealthDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HealthDataViewModel::class.java]
        _binding = FragmentHealthDataBinding.inflate(inflater, container, false)
        
        setupChart()
        observeData()
        
        binding.stopMonitoring.setOnClickListener {
            viewModel.stopGeneratingTestData()
        }
        
        viewModel.startGeneratingTestData()
        
        return binding.root
    }

    private fun setupChart() {
        lineChart = binding.heartRateChart
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            setDrawGridBackground(false)
            
            // 设置动画
            animateX(1000)
            
            // 固定X轴范围
            xAxis.apply {
                axisMinimum = 0f
                axisMaximum = 20f  // 固定显示20秒
                labelCount = 5     // 显示5个刻度
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${(20 - value).toInt()}秒前"  // 显示"x秒前"
                    }
                }
            }
            
            // 禁用缩放和拖动，因为我们要固定显示范围
            setScaleEnabled(false)
            isDragEnabled = false
            
            // 配置左Y轴
            axisLeft.apply {
                textColor = Color.GRAY
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
                axisMinimum = 50f
                axisMaximum = 100f
            }
            
            // 禁用右Y轴
            axisRight.isEnabled = false
            
            // 设置图例
            legend.isEnabled = false
            
            // 设置空数据提示
            setNoDataText("暂无数据")
        }
    }

    private fun observeData() {
        viewModel.heartRateData.observe(viewLifecycleOwner) { entries ->
            val dataSet = LineDataSet(entries, "心率").apply {
                color = Color.RED
                lineWidth = 2f
                setDrawCircles(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillColor = Color.RED
                fillAlpha = 30
            }
            
            lineChart.data = LineData(dataSet)
            lineChart.moveViewToX(entries.last().x)  // 移动到最新数据点
            lineChart.invalidate()
            
            // 更新当前心率显示
            entries.lastOrNull()?.let { lastEntry ->
                binding.currentHeartRate.text = "${lastEntry.y.toInt()} BPM"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopGeneratingTestData()
        _binding = null
    }
} 