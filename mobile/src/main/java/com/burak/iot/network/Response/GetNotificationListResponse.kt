package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class GetNotificationListResponse(

        @field:SerializedName("result")
        val notificationResult: NotificationResult? = null,

        @field:SerializedName("status")
        val status: Status? = null
)