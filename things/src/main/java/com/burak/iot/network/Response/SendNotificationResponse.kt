package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class SendNotificationResponse(

        @field:SerializedName("status")
        val status: Status? = null
)