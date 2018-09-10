package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class Status(

        @field:SerializedName("code")
        val code: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)