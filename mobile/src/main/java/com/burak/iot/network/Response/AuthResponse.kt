package com.burak.iot.network.Response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

        @field:SerializedName("result")
        val authResult: AuthResult? = null,

        @field:SerializedName("status")
        val status: Status? = null
)