package com.burak.iot.network

/**
 * Created by burakakgun on 28.08.2018.
 */
import com.burak.iot.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkRepository {
    // private val baseUrl = "place your gateway"
//    private val baseUrl = "http://192.168.210.249:8080/"
    private val baseUrl = "https://things.mbakgun.com/IOT/"
    private val client = OkHttpClient().newBuilder()
            .cache(null)
            .addInterceptor { chain -> chain.proceed(chain.request().newBuilder().header("platform", "android").build()) }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val iotService = retrofit.create(IotService::class.java)
}