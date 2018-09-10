package com.burak.iot.network.response

import com.burak.iot.model.device.Device
import com.google.gson.annotations.SerializedName

data class AuthResult(

        @field:SerializedName("device")
        val device: Device? = null
)