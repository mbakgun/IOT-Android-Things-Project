package com.burak.iot.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.burak.iot.model.notification.Notification
import com.burak.iot.model.notification.NotificationProcessor
import com.burak.iot.utils.DBHelper

class NotificationViewModel @JvmOverloads constructor(app: Application, val notificationProcessor: NotificationProcessor = NotificationProcessor()) : ObservableViewModel(app), NotificationProcessor.OnNotificationListener {
    var lastNotificationData: MutableList<Notification> = ArrayList()
    var deviceListener: NotificationProcessor.OnNotificationListener? = null
    val db by lazy { DBHelper(app) }
    override val generatedTokens: MutableList<String> get() = db.getTokens()

    init {
        deviceListener = this
        updateOutputs(db.getNotifications())
    }

    override fun onSuccess(notification: MutableList<Notification>) {
        db.insertNotification(notification)
        notificationProcessor.saveNotification(notification)
        updateOutputs(notification)
    }

    fun updateOutputs(nd: MutableList<Notification>) {
        lastNotificationData = nd
        notifyChange()
    }

    fun loadNotificationSummaries(): LiveData<List<Notification>> {
        notificationProcessor.mergeLocalNotificationList(db.getNotifications())
        return Transformations.map(notificationProcessor.loadNotifications()) { notificationObjects ->
            notificationObjects.map { it }
        }
    }
}