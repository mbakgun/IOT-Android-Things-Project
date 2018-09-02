package com.burak.iot.model.notification

data class Notification(val deviceId: String = "",
                        val name: String? = null,
                        val imageUrl: String = "",
                        val sentDate: Long)