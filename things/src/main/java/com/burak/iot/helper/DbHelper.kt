package com.burak.iot.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by burakakgun on 28.08.2018.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("DB", "Upgrade from version $oldVersion to $newVersion")
        Log.w("DB", "This is version 1, no DB to update")
    }

    private val TABLE_NAME = "IotThings"
    private val COL_GENERATED_TOKEN = "generatedToken"

    companion object {
        private val DATABASE_NAME = "SQLITE_DATABASE"
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_GENERATED_TOKEN VARCHAR(256) PRIMARY KEY )"
        db?.execSQL(createTable)
    }

    fun insertData(generatedToken: String) {
        val sqliteDB = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_GENERATED_TOKEN, generatedToken)
        val result = sqliteDB.insert(TABLE_NAME, null, contentValues)
    }

    fun getToken(): String {
        val sqliteDB = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            return result.getString(result.getColumnIndex(COL_GENERATED_TOKEN))
        }
        result.close()
        sqliteDB.close()
        return ""
    }
}