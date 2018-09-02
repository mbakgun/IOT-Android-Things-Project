package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class NotificationResult(

        @field:SerializedName("notifications")
        val notifications: List<NotificationsItem?>? = null
)