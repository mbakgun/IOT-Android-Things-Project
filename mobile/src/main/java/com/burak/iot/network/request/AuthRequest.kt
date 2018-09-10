package com.burak.iot.network.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(

        @field:SerializedName("deviceId")
        var deviceId: String? = null,

        @field:SerializedName("fcmToken")
        var fcmToken: String? = null

)