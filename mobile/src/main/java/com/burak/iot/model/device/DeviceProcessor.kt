package com.burak.iot.model.device

import android.arch.lifecycle.LiveData
import android.util.Log
import com.burak.iot.network.NetworkRepository
import com.burak.iot.network.Response.AuthRequest
import com.burak.iot.network.Response.AuthResponse
import com.burak.iot.network.Response.ConfigureRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceProcessor(val repository: DeviceRepository = DeviceRepository(), val networkRepository: NetworkRepository = NetworkRepository()) {

    interface OnDeviceListener {
        val fcmToken: String
        fun onSuccess(device: Device)
    }

    fun saveDevice(tc: Device) {
        repository.saveOrUpdateDevice(tc)
    }

    fun loadDevices(): LiveData<List<Device>> {
        return repository.loadDevices()
    }

    fun mergeLocalDevicesList(dataList: List<Device>) {
        repository.mergeLocalDevicesList(dataList)
    }

    fun addDeviceFromNetwork(onDeviceListener: OnDeviceListener, deviceId: String) {
        val call = networkRepository.iotService.authDevice(AuthRequest(deviceId, onDeviceListener.fcmToken))
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                if (response != null && response.isSuccessful) {
                    response.body()?.authResult?.device?.let { onDeviceListener.onSuccess(it) }
                } else {
                    onFailure(call, Throwable("Bad Response"))
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.e("Response Failure", t?.message, t)
            }
        })
    }

    fun configureDeviceInNetwork(onDeviceListener: OnDeviceListener, device: Device) {
        val call = networkRepository.iotService.configureDevice(device.generatedToken, ConfigureRequest(device.name, device.active))
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                if (response != null && response.isSuccessful) {
                    response.body()?.authResult?.device?.let { onDeviceListener.onSuccess(it) }
                } else {
                    onFailure(call, Throwable("Bad Response"))
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.e("Response Failure", t?.message, t)
            }
        })
    }

}