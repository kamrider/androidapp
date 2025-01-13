package com.example.okok.ui.home

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.okok.R

class DeviceListAdapter(
    private var devices: List<BluetoothDeviceItem>,
    private val onDeviceClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    data class BluetoothDeviceItem(
        val device: BluetoothDevice,
        val isConnected: Boolean = false
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView = view.findViewById(R.id.device_name)
        val deviceAddress: TextView = view.findViewById(R.id.device_address)
        val container: View = view.findViewById(R.id.device_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bluetooth_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceItem = devices[position]
        
        // 设置设备名称和地址
        holder.deviceName.text = deviceItem.device.name ?: "未知设备"
        holder.deviceAddress.text = deviceItem.device.address
        
        // 根据连接状态设置样式
        if (deviceItem.isConnected) {
            holder.itemView.setBackgroundResource(R.drawable.connected_device_background)
            holder.container.alpha = 1.0f
        } else {
            holder.itemView.setBackgroundResource(0)
            holder.container.alpha = 0.7f
        }
        
        holder.container.setOnClickListener {
            onDeviceClick(deviceItem.device)
        }
    }

    override fun getItemCount() = devices.size

    fun updateDevices(newDevices: List<BluetoothDevice>, connectedDevice: BluetoothDevice?) {
        // 创建新的设备列表
        devices = newDevices.map { device ->
            BluetoothDeviceItem(
                device = device,
                isConnected = device.address == connectedDevice?.address
            )
        }.sortedByDescending { it.isConnected }  // 已连接设备置顶
        
        // 立即强制刷新整个列表
        notifyDataSetChanged()
    }
}
