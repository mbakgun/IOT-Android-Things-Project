package com.burak.iot.network

/**
 * Created by burakakgun on 28.08.2018.
 */

import com.burak.iot.network.Response.AuthRequest
import com.burak.iot.network.Response.AuthResponse
import com.burak.iot.network.Response.SendNotificationResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IotService {

    @POST("authorize.json")
    fun authDevice(@Body request: AuthRequest): Call<AuthResponse>

    @Multipart
    @POST("api/sendNotification.json")
    fun sendNotification(@Header("x-token") authorization: String, @Part image: MultipartBody.Part): Call<SendNotificationResponse>
}