package com.example.tk3.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tk3.model.DestinationListModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        private val DB_NAME = "destination"
        private val DB_VERSION = 1
        private val TABLE_NAME = "destinationlist"
        private val ID = "id"
        private val DESTINATION_NAME = "name"
        private val DESTINATION_DESCRIPTION = "description"
        private val DESTINATION_LONG = "longitude"
        private val DESTINATION_LAT = "latitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $DESTINATION_NAME TEXT, $DESTINATION_DESCRIPTION TEXT, $DESTINATION_LONG REAL, $DESTINATION_LAT REAL);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllDestination(): List<DestinationListModel> {
        val destinationlist = ArrayList<DestinationListModel>()
        val db:SQLiteDatabase = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor:Cursor = db.rawQuery(selectQuery, null)
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val destinations = DestinationListModel()
                    destinations.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    destinations.name =
                        cursor.getString(cursor.getColumnIndex(DESTINATION_NAME))
                    destinations.description = cursor.getString(
                        cursor.getColumnIndex(
                            DESTINATION_DESCRIPTION
                        )
                    )
                    destinations.latitude = cursor.getDouble(
                        cursor.getColumnIndex(
                            DESTINATION_LAT
                        )
                    )
                    destinations.longitude = cursor.getDouble(
                        cursor.getColumnIndex(
                            DESTINATION_LONG
                        )
                    )
                    destinationlist.add(destinations)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return destinationlist
    }

    fun addDestination(destinations: DestinationListModel) : Boolean {
        val db:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(DESTINATION_NAME, destinations.name)
        values.put(DESTINATION_DESCRIPTION, destinations.description)
        values.put(DESTINATION_LAT, destinations.latitude)
        values.put(DESTINATION_LONG, destinations.longitude)
        val _success:Long = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    @SuppressLint("Range")
    fun getDestination(_id: Int) : DestinationListModel {
        val destinations = DestinationListModel()
        val db:SQLiteDatabase = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor:Cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        destinations.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        destinations.name =
            cursor.getString(cursor.getColumnIndex(DESTINATION_NAME))
        destinations.description = cursor.getString(
            cursor.getColumnIndex(
                DESTINATION_DESCRIPTION
            )
        )
        destinations.latitude = cursor.getDouble(
            cursor.getColumnIndex(
                DESTINATION_LAT
            )
        )
        destinations.longitude = cursor.getDouble(
            cursor.getColumnIndex(
                DESTINATION_LONG
            )
        )
        cursor.close()
        return destinations
    }

    fun deleteDestination(_id: Int) : Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun updateDestination(destinations: DestinationListModel) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DESTINATION_NAME, destinations.name)
        values.put(DESTINATION_DESCRIPTION, destinations.description)
        values.put(DESTINATION_LAT, destinations.latitude)
        values.put(DESTINATION_LONG, destinations.longitude)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(destinations.id.toString()))
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}