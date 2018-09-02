package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class AuthRequest(

	@field:SerializedName("deviceId")
    var deviceId: String? = null
)