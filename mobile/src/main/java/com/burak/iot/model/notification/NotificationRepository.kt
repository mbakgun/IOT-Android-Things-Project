package com.burak.iot.model.notification

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class NotificationRepository {

    private val savedNotifications = mutableMapOf<Long, Notification>()
    val liveData = MutableLiveData<List<Notification>>()

    fun saveNotification(notification: MutableList<Notification>) {
        notification.forEach { notificationItem -> savedNotifications[notificationItem.sentDate] = notificationItem }
        liveData.value = savedNotifications.values.toList()
    }

    fun loadNotifications(): LiveData<List<Notification>> {
        liveData.value = savedNotifications.values.toList()
        return liveData
    }

    fun mergeLocalNotificationList(dataList: List<Notification>) {
        dataList.forEach { notificationData ->
            savedNotifications[notificationData.sentDate] = notificationData
        }
        liveData.value = savedNotifications.values.toList()
    }
}