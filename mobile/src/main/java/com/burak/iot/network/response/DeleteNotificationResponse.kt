package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class DeleteNotificationResponse(

	@field:SerializedName("result")
	val result: DeletionResult? = null,

	@field:SerializedName("status")
	val status: Status? = null
)