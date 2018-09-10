package com.burak.iot.network.request

import com.google.gson.annotations.SerializedName

data class DeleteNotificationRequest(

        @field:SerializedName("sentDate")
        var sentDate: Long? = null
)