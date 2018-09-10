package com.burak.iot.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.burak.iot.model.device.Device
import com.burak.iot.model.notification.Notification

/**
 * Created by burakakgun on 8/15/2018.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("DB", "Upgrade from version $oldVersion to $newVersion")
        Log.w("DB", "This is version 1, no DB to update")
    }


    //tables
    private val TABLE_NAME_DEVICE = "Devices"
    private val TABLE_NAME_NOTIFICATION = "Notifications"

    //device
    private val COL_DEVICE_ID = "deviceId"
    private val COL_NAME = "name"
    private val COL_ACTIVATE = "active"
    private val COL_GENERATED_TOKEN = "generatedToken"
    private val COL_CREATE_DATE = "createDate"

    //notification
    private val COL_IMAGE_URL = "imageUrl"
    private val COL_SENT_DATE = "sentDate"


    companion object {
        private val DATABASE_NAME = "SQLITE_DATABASE"
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createDeviceTable = "CREATE TABLE if not exists $TABLE_NAME_DEVICE ( $COL_DEVICE_ID  VARCHAR(256)  PRIMARY KEY ,$COL_NAME  VARCHAR(256),$COL_ACTIVATE  VARCHAR(256), $COL_GENERATED_TOKEN  VARCHAR(256),$COL_CREATE_DATE LONG)"
        db?.execSQL(createDeviceTable)

        val createNotificationTable = "CREATE TABLE if not exists $TABLE_NAME_NOTIFICATION ($COL_SENT_DATE LONG PRIMARY KEY,$COL_DEVICE_ID VARCHAR(256),$COL_NAME  VARCHAR(256), $COL_IMAGE_URL  VARCHAR(256))"
        db?.execSQL(createNotificationTable)
    }

    fun insertDevice(device: Device) {
        val sqliteDB = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_DEVICE_ID, device.deviceId)
        contentValues.put(COL_NAME, device.name)
        contentValues.put(COL_ACTIVATE, device.active.toString())
        contentValues.put(COL_GENERATED_TOKEN, device.generatedToken)
        contentValues.put(COL_CREATE_DATE, device.createDate)
        val result = sqliteDB.insertWithOnConflict(TABLE_NAME_DEVICE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun insertNotification(notifications: MutableList<Notification>) {
        val sqliteDB = writableDatabase
        notifications.forEach { notification ->
            val contentValues = ContentValues()
            contentValues.put(COL_DEVICE_ID, notification.deviceId)
            contentValues.put(COL_NAME, notification.name)
            contentValues.put(COL_IMAGE_URL, notification.imageUrl)
            contentValues.put(COL_SENT_DATE, notification.sentDate)
            val result = sqliteDB.insertWithOnConflict(TABLE_NAME_NOTIFICATION, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        }
    }

    fun getTokens(): MutableList<String> {
        val tokenList = mutableListOf<String>()
        val sqliteDB = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DEVICE"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val token = result.getString(result.getColumnIndex(COL_GENERATED_TOKEN))
                tokenList.add(token)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return tokenList
    }

    fun getNotifications(): MutableList<Notification> {
        val notificationList = mutableListOf<Notification>()
        val sqliteDB = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_NOTIFICATION"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val notification = Notification(name = result.getString(result.getColumnIndex(COL_NAME)), sentDate = (result.getLong(result.getColumnIndex(COL_SENT_DATE))), deviceId = result.getString(result.getColumnIndex(COL_DEVICE_ID)), imageUrl = result.getString(result.getColumnIndex(COL_IMAGE_URL)))
                notificationList.add(notification)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return notificationList
    }

    fun getDevices(): MutableList<Device> {
        val deviceList = mutableListOf<Device>()
        val sqliteDB = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DEVICE"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val notification = Device(name = result.getString(result.getColumnIndex(COL_NAME)), deviceId = result.getString(result.getColumnIndex(COL_DEVICE_ID)), active = result.getString(result.getColumnIndex(COL_ACTIVATE))!!.toBoolean(), generatedToken = result.getString(result.getColumnIndex(COL_GENERATED_TOKEN)), createDate = result.getLong(result.getColumnIndex(COL_CREATE_DATE)))
                deviceList.add(notification)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return deviceList
    }

    fun deleteNotification(sentDate: Long) {
        val sqliteDB = writableDatabase
        sqliteDB.execSQL("DELETE FROM $TABLE_NAME_NOTIFICATION WHERE $COL_SENT_DATE= '$sentDate'");
        sqliteDB.close()
    }
}