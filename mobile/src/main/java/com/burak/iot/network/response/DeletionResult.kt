package com.burak.iot.network.response

import com.google.gson.annotations.SerializedName

data class DeletionResult(

	@field:SerializedName("deleteStatus")
	val deleteStatus: Boolean? = null
)