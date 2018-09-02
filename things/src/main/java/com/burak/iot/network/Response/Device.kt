package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class Device(

        @field:SerializedName("generatedToken")
        val generatedToken: String? = null
)