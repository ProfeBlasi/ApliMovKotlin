package com.example.tpintegrador.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "School.db"
        const val TABLE_NAME_STUDENT = "student"
        const val COLUMN_ID_STUDENT = "id"
        const val COLUMN_LAST_NAME_STUDENT = "last_name"
        const val COLUMN_NAME_STUDENT = "name_student"
        const val COLUMN_ADDRESS_STUDENT = "address"
        const val COLUMN_PHONE_STUDENT = "phone"
        const val COLUMN_NATIONALITY_STUDENT = "nationality"
        const val COLUMN_BIRTHDATE_STUDENT = "birthdate"
        const val COLUMN_DOCUMENT_STUDENT = "document"
        const val COLUMN_EMAIL_STUDENT = "email"
        const val COLUMN_STATE_STUDENT = "state"
        const val COLUMN_COURSE_ID_STUDENT = "course_id"

        const val TABLE_NAME_COURSE = "courses"
        const val COLUMN_ID_COURSE = "id"
        const val COLUMN_NAME_COURSE = "name"
        const val COLUMN_NAME_SCHOOL = "school"
        const val COLUMN_NAME_SHIFT = "shift"
        const val COLUMN_NAME_ADDRESS = "address"
        const val COLUMN_NAME_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStudentQuery = ("CREATE TABLE $TABLE_NAME_STUDENT ("
                + "$COLUMN_ID_STUDENT INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_LAST_NAME_STUDENT TEXT NOT NULL,"
                + "$COLUMN_NAME_STUDENT TEXT NOT NULL,"
                + "$COLUMN_ADDRESS_STUDENT TEXT,"
                + "$COLUMN_PHONE_STUDENT TEXT,"
                + "$COLUMN_NATIONALITY_STUDENT TEXT,"
                + "$COLUMN_BIRTHDATE_STUDENT TEXT,"
                + "$COLUMN_DOCUMENT_STUDENT TEXT,"
                + "$COLUMN_EMAIL_STUDENT TEXT,"
                + "$COLUMN_STATE_STUDENT TEXT,"
                + "$COLUMN_COURSE_ID_STUDENT TEXT);")
        db.execSQL(createTableStudentQuery)
        val createTableCourseQuery = ("CREATE TABLE $TABLE_NAME_COURSE ("
                + "$COLUMN_ID_COURSE INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME_COURSE TEXT NOT NULL,"
                + "$COLUMN_NAME_SCHOOL TEXT,"
                + "$COLUMN_NAME_SHIFT TEXT,"
                + "$COLUMN_NAME_ADDRESS TEXT,"
                + "$COLUMN_NAME_USER_ID TEXT);")
        db.execSQL(createTableCourseQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_STUDENT")
        onCreate(db)
    }
}