package com.burak.iot.model.device

data class Device(val deviceId: String = "",
                  var name: String = "",
                  var active: Boolean = true,
                  val generatedToken: String = "",
                  val createDate: Long)