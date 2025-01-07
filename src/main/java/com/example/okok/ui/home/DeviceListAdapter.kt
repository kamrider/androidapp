package com.example.okok.ui.home

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.okok.R

class DeviceListAdapter(
    private var devices: List<BluetoothDevice>,
    private val onDeviceSelected: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    private var selectedPosition = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView = view.findViewById(R.id.device_name)
        val deviceAddress: TextView = view.findViewById(R.id.device_address)
        val deviceSelect: RadioButton = view.findViewById(R.id.device_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bluetooth_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.deviceName.text = device.name ?: "未知设备"
        holder.deviceAddress.text = device.address
        holder.deviceSelect.isChecked = position == selectedPosition

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onDeviceSelected(device)
        }
    }

    override fun getItemCount() = devices.size

    fun updateDevices(newDevices: List<BluetoothDevice>) {
        devices = newDevices
        notifyDataSetChanged()
    }
}
