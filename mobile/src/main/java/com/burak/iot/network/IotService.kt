package com.burak.iot.network

/**
 * Created by burakakgun on 28.08.2018.
 */

import com.burak.iot.network.request.AuthRequest
import com.burak.iot.network.request.ConfigureRequest
import com.burak.iot.network.request.DeleteNotificationRequest
import com.burak.iot.network.response.AuthResponse
import com.burak.iot.network.response.DeleteNotificationResponse
import com.burak.iot.network.response.GetNotificationListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IotService {

    @POST("authorize.json")
    fun authDevice(@Body request: AuthRequest): Call<AuthResponse>

    @GET("api/getNotificationList.json")
    fun getNotificationList(@Header("x-token") authorization: String): Call<GetNotificationListResponse>

    @POST("api/configure.json")
    fun configureDevice(@Header("x-token") authorization: String, @Body request: ConfigureRequest): Call<AuthResponse>

    @POST("api/deleteNotification.json")
    fun deleteNotification(@Header("x-token") authorization: String, @Body request: DeleteNotificationRequest): Call<DeleteNotificationResponse>

}