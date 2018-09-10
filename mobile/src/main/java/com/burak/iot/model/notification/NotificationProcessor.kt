package com.burak.iot.model.notification

import android.arch.lifecycle.LiveData
import android.text.TextUtils
import android.util.Log
import com.burak.iot.network.NetworkRepository
import com.burak.iot.network.request.DeleteNotificationRequest
import com.burak.iot.network.response.DeleteNotificationResponse
import com.burak.iot.network.response.GetNotificationListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationProcessor(val repository: NotificationRepository = NotificationRepository(), val networkRepository: NetworkRepository = NetworkRepository()) {

    interface OnNotificationListener {
        val generatedTokens: MutableList<String>
        fun onSuccess(notification: MutableList<Notification>)
        fun onDelete(sentDate: Long)
    }

    fun saveNotification(notification: MutableList<Notification>) {
        repository.saveNotification(notification)
    }

    fun deleteNotification(onNotificationListener: OnNotificationListener, sentDate: Long) {
        onNotificationListener.generatedTokens.forEach { generatedToken ->
            val call = networkRepository.iotService.deleteNotification(generatedToken, DeleteNotificationRequest(sentDate))
            call.enqueue(object : Callback<DeleteNotificationResponse> {
                override fun onResponse(call: Call<DeleteNotificationResponse>?, response: Response<DeleteNotificationResponse>?) =
                        if (response != null && response.isSuccessful) {
                            val result = response.body()?.result?.deleteStatus
                            when (result) {
                                true -> {
                                    repository.deleteNotification(sentDate)
                                    onNotificationListener.onDelete(sentDate)
                                }
                                false -> onFailure(call, Throwable("Bad Request"))
                                null -> onFailure(call, Throwable("Bad Request"))
                            }
                        } else {
                            onFailure(call, Throwable("Bad Response"))
                        }

                override fun onFailure(call: Call<DeleteNotificationResponse>?, t: Throwable?) {
                    Log.e("Response Failure", t?.message, t)
                }
            })
        }
    }

    fun mergeLocalNotificationList(dataList: MutableList<Notification>) {
        repository.mergeLocalNotificationList(dataList)
    }

    fun loadNotifications(): LiveData<List<Notification>> {
        return repository.loadNotifications()
    }

    fun getNotificationList(onNotificationListener: OnNotificationListener) {
        onNotificationListener.generatedTokens.forEach { generatedToken ->
            val call = networkRepository.iotService.getNotificationList(generatedToken)
            call.enqueue(object : Callback<GetNotificationListResponse> {
                override fun onResponse(call: Call<GetNotificationListResponse>?, response: Response<GetNotificationListResponse>?) =
                        if (response != null && response.isSuccessful) {
                            val notificationListResponse = response.body()?.notificationResult?.notifications
                            val notifications: ArrayList<Notification> = ArrayList()
                            notificationListResponse?.forEach { notificationsItem ->
                                val name: String? = if (TextUtils.isEmpty(notificationsItem!!.name).not()) notificationsItem.name else null
                                notifications += Notification(
                                        deviceId = notificationsItem.deviceId!!,
                                        name = name,
                                        imageUrl = notificationsItem.imageUrl!!,
                                        sentDate = notificationsItem.sentDate!!
                                )
                            }
                            onNotificationListener.onSuccess(notifications)
                        } else {
                            onFailure(call, Throwable("Bad Response"))
                        }

                override fun onFailure(call: Call<GetNotificationListResponse>?, t: Throwable?) {
                    Log.e("Response Failure", t?.message, t)
                }
            })
        }
    }
}