package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

        @field:SerializedName("result")
        val result: Result? = null,

        @field:SerializedName("status")
        val status: Status? = null
)