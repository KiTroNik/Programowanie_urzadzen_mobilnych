package com.example.weatherapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavDb(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private const val DATABASE_VER = 1
        private const val DATABASE_NAME = "WEATHER.db"

        private const val TABLE_NAME = "Favorites"
        private const val COL_NAME = "Name"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY: String = ("CREATE TABLE $TABLE_NAME ($COL_NAME  TEXT)")
        p0!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    val favoriteList: ArrayList<String>
        @SuppressLint("Range")
        get() {
            val favList = ArrayList<String>()
            val selectQuery = "SELECT $COL_NAME FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()) {
                do {
                    favList.add(cursor.getString(cursor.getColumnIndex(COL_NAME)))
                } while (cursor.moveToNext())
            }
            return favList
        }

    fun addLocalization(loc: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, loc)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteLocalization(loc: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_NAME=?", arrayOf(loc))
        db.close()
    }
}
