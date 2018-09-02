package com.burak.iot.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.burak.iot.model.device.Device
import com.burak.iot.model.device.DeviceProcessor
import com.burak.iot.utils.DBHelper
import com.google.firebase.iid.FirebaseInstanceId

class DeviceViewModel @JvmOverloads constructor(app: Application, val deviceProcessor: DeviceProcessor = DeviceProcessor()) : ObservableViewModel(app), DeviceProcessor.OnDeviceListener {
    override val fcmToken: String get() = FirebaseInstanceId.getInstance().token!!
    var deviceListener: DeviceProcessor.OnDeviceListener? = null
    val db by lazy { DBHelper(app) }

    init {
        deviceListener = this
    }

    override fun onSuccess(device: Device) {
        db.insertDevice(device)
        deviceProcessor.saveDevice(device)
    }


    fun addDevice(deviceId: String) {
        deviceProcessor.addDeviceFromNetwork(this, deviceId)
    }

    fun changeDevice(device: Device) {
        deviceProcessor.configureDeviceInNetwork(this, device)
    }

    fun loadDevicesSummaries(): LiveData<List<Device>> {
        deviceProcessor.mergeLocalDevicesList(db.getDevices())
        return Transformations.map(deviceProcessor.loadDevices()) { deviceObjects ->
            deviceObjects.map { it }
        }
    }
}