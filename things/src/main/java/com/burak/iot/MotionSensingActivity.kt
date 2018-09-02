package com.burak.iot

import android.content.Context
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.burak.iot.camera.CustomCamera
import com.leinardi.android.things.driver.hcsr04.Hcsr04SensorDriver
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import java.io.IOException
import java.util.*


class MotionSensingActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var camera: CustomCamera
    private lateinit var motionViewModel: MotionSensingViewModel

    private lateinit var mProximitySensorDriver: Hcsr04SensorDriver
    private lateinit var mSensorManager: SensorManager

    private val mDynamicSensorCallback = object : SensorManager.DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            if (sensor.type == Sensor.TYPE_PROXIMITY) {
                Log.i(TAG_DISTANCE, "Proximity sensor connected")
                mSensorManager.registerListener(this@MotionSensingActivity,
                        sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.app_name)
        setupViewModel()
        auth()
        setupCamera()
        setupSensors()
    }

    private fun setupViewModel() {
        motionViewModel = MotionSensingViewModel(application)
        //    motionViewModel = ViewModelProviders.of(this).get(MotionSensingViewModel::class.java)
    }

    private fun setupSensors() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.registerDynamicSensorCallback(mDynamicSensorCallback)
        try {
            mProximitySensorDriver = Hcsr04SensorDriver("BCM21", "BCM20");
            mProximitySensorDriver.registerProximitySensor();
        } catch (e: Exception) {
            Log.e(TAG_DISTANCE, "Error configuring sensor", e)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG_DISTANCE, "Closing sensor")
        mSensorManager.unregisterDynamicSensorCallback(mDynamicSensorCallback)
        mSensorManager.unregisterListener(this)
        mProximitySensorDriver.unregisterProximitySensor()
        try {
            mProximitySensorDriver.close()
        } catch (e: IOException) {
            Log.e(TAG_DISTANCE, "Error closing sensor", e)
        }
    }

    private fun setupCamera() {
        camera = CustomCamera.getInstance()
        camera.initializeCamera(this, Handler(), imageAvailableListener)
    }

    private fun auth() {
        if (StringUtils.isEmpty(motionViewModel.db.getToken())) {
            motionViewModel.authDevice(DEVICE_ID)
        } else {
            Log.d(DEVICE_ID, motionViewModel.db.getToken())
        }
    }

    private val imageAvailableListener = object : CustomCamera.ImageCapturedListener {
        override fun onImageCaptured(bitmap: Bitmap) {
            motionViewModel.uploadMotionImage(bitmap)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i(TAG_DISTANCE, "sensor accuracy changed: $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            // every 15 seconds with meaningful value
            if (event.values[0] > 1 && event.values[0] < 100 && Date().after(DateUtils.addSeconds(lastTakenTimeStamp, 15))) {
                Log.i(TAG_DISTANCE, String.format(Locale.getDefault(), "sensor changed: [%f]", event.values[0]))
                lastTakenTimeStamp = Date()
                camera.takePicture()
            }
        }
    }

    companion object {
        val TAG_DISTANCE = "DISTANCE"
        val DEVICE_ID = "home-camera" //replace with yours or register device over gateway
        var lastTakenTimeStamp: Date = Date()
    }
}