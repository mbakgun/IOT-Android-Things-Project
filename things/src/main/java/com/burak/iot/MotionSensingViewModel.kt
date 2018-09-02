package com.burak.iot

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.burak.iot.helper.DBHelper
import com.burak.iot.network.NetworkRepository
import com.burak.iot.network.Response.AuthRequest
import com.burak.iot.network.Response.AuthResponse
import com.burak.iot.network.Response.SendNotificationResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class MotionSensingViewModel @JvmOverloads constructor(app: Application, val networkRepository: NetworkRepository = NetworkRepository()) : ViewModel() {
    val db by lazy { DBHelper(app) }

    fun uploadMotionImage(imageBytes: Bitmap) {
        val file = File(Environment.getExternalStorageDirectory().absolutePath, "takenImage.jpeg").writeBitmap(imageBytes, Bitmap.CompressFormat.JPEG, 100)
        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val body = MultipartBody.Part.createFormData("img", file.getName(), reqFile)
        val call = networkRepository.iotService.sendNotification(db.getToken(), body)
        call.enqueue(object : Callback<SendNotificationResponse> {
            override fun onResponse(call: Call<SendNotificationResponse>?, response: Response<SendNotificationResponse>?) {
                if (response != null && response.isSuccessful) {
                    val notificationResponse = response.body()
                } else {
                    onFailure(call, Throwable("Bad Response"))
                }
            }

            override fun onFailure(call: Call<SendNotificationResponse>?, t: Throwable?) {
                Log.e("Response Failure", t?.message, t)
            }
        })
    }

    fun authDevice(deviceId: String) {
        val request = AuthRequest(deviceId)
        val call = networkRepository.iotService.authDevice(request)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                if (response != null && response.isSuccessful) {
                    val authResponse = response.body()
                    db.insertData(authResponse!!.result!!.device!!.generatedToken!!)
                } else {
                    onFailure(call, Throwable("Bad Response"))
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Log.e("Response Failure", t?.message, t)
            }
        })
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) = apply {
        val out = FileOutputStream(this)
        bitmap.compress(format, quality, out)
        out.flush()
        out.close()
    }
}