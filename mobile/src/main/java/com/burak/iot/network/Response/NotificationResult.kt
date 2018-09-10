package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class NotificationResult(

        @field:SerializedName("notifications")
        val notifications: List<NotificationsItem?>? = null
)