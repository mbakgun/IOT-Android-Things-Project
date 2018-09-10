package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class NotificationsItem(

        @field:SerializedName("sentDate")
        val sentDate: Long? = null,

        @field:SerializedName("imageUrl")
        val imageUrl: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("deviceId")
        val deviceId: String? = null
)