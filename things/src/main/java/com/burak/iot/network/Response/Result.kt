package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class Result(

        @field:SerializedName("device")
        val device: Device? = null
)