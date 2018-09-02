package com.burak.iot.network

/**
 * Created by burakakgun on 28.08.2018.
 */

import com.burak.iot.network.Response.AuthRequest
import com.burak.iot.network.Response.AuthResponse
import com.burak.iot.network.Response.ConfigureRequest
import com.burak.iot.network.Response.GetNotificationListResponse
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

}