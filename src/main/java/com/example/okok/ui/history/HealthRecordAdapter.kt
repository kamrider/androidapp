package com.example.okok.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.okok.databinding.ItemHealthRecordBinding

class HealthRecordAdapter(
    private var records: List<HealthRecord>,
    private val onItemClick: (HealthRecord) -> Unit
) : RecyclerView.Adapter<HealthRecordAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemHealthRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: HealthRecord) {
            binding.textDate.text = record.date
            binding.textHeartRate.text = "心率: ${record.heartRate} BPM"
            binding.textBloodOxygen.text = "血氧: ${record.bloodOxygen}%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHealthRecordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record)
        holder.itemView.setOnClickListener { onItemClick(record) }
    }

    override fun getItemCount() = records.size

    fun updateData(newRecords: List<HealthRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }
} 