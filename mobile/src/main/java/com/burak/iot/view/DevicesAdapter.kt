package com.burak.iot.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burak.iot.R
import com.burak.iot.databinding.SavedDevicesListItemBinding
import com.burak.iot.model.device.Device


class DevicesAdapter(val onItemChanged: (item: Device) -> Unit)
    : RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder>() {

    private val deviceSummaries = mutableListOf<Device>()

    fun updateList(updates: List<Device>) {
        deviceSummaries.clear()
        deviceSummaries.addAll(updates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SavedDevicesListItemBinding>(
                inflater, R.layout.saved_devices_list_item, parent, false)
        return DevicesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deviceSummaries.size
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.bind(deviceSummaries[position])
    }

    inner class DevicesViewHolder(val binding: SavedDevicesListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Device) {
            binding.item = item
            binding.executePendingBindings()
            binding.imageViewSave.setOnClickListener {
                item.active = binding.switch1.isChecked
                onItemChanged(item)
            }
        }
    }
}