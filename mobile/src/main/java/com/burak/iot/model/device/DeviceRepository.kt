package com.burak.iot.model.device

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class DeviceRepository {

    private val savedDevices = mutableMapOf<String, Device>()
    val liveData = MutableLiveData<List<Device>>()

    fun saveOrUpdateDevice(tc: Device) {
        savedDevices[tc.deviceId] = tc
        liveData.value = savedDevices.values.toList()
    }

    fun loadDeviceByDeviceId(deviceId: String): Device? {
        return savedDevices[deviceId]
    }

    fun loadDevices(): LiveData<List<Device>> {
        liveData.value = savedDevices.values.toList()
        return liveData
    }

    fun mergeLocalDevicesList(dataList: List<Device>) {
        dataList.forEach { deviceData ->
            savedDevices[deviceData.deviceId] = deviceData
        }
        liveData.value = savedDevices.values.toList()
    }
}