package com.example.tpintegrador.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val TABLE_NAME_COURSE = "courses"
        const val COLUMN_ID_COURSE = "id"
        const val COLUMN_NAME_COURSE = "name"
        const val COLUMN_NAME_SCHOOL = "school"
        const val COLUMN_NAME_SHIFT = "shift"
        const val COLUMN_NAME_ADDRESS = "address"
        const val COLUMN_NAME_USER_ID = "user_id"

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "school.db"
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

        const val TABLE_NAME_ATTENDANCE_STUDENT = "attendance_student"
        const val COLUMN_ID_ATTENDANCE_STUDENT_ID = "id"
        const val COLUMN_ATTENDANCE_DAY = "attendance_day"
        const val COLUMN_STUDENT_ID = "student_id"
        const val COLUMN_ATTENDANCE_COURSE_ID = "attendance_course_id"
        const val COLUMN_ATTENDANCE_STUDENT_STATUS = "attendance_student_status"
        const val COLUMN_REFERENCE_ID = "reference_id"

        const val TABLE_NAME_REFERENCE = "references_table"
        const val COLUMN_ID_REFERENCE = "id"
        const val COLUMN_REFERENCE_DESCRIPTION = "description"
        const val COLUMN_REFERENCE_OPENING_DATE = "opening_date"
        const val COLUMN_REFERENCE_CLOSING_DATE = "closing_date"
        const val COLUMN_REFERENCE_CLOSE = "close"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTableCourseQuery = ("CREATE TABLE $TABLE_NAME_COURSE ("
                + "$COLUMN_ID_COURSE INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME_COURSE TEXT NOT NULL,"
                + "$COLUMN_NAME_SCHOOL TEXT,"
                + "$COLUMN_NAME_SHIFT TEXT,"
                + "$COLUMN_NAME_ADDRESS TEXT,"
                + "$COLUMN_NAME_USER_ID TEXT);")
        db.execSQL(createTableCourseQuery)

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
                + "$COLUMN_COURSE_ID_STUDENT INTEGER,"
                + "FOREIGN KEY($COLUMN_COURSE_ID_STUDENT) REFERENCES $TABLE_NAME_COURSE($COLUMN_ID_COURSE));")
        db.execSQL(createTableStudentQuery)

        val createTableAttendanceStudentQuery = ("CREATE TABLE $TABLE_NAME_ATTENDANCE_STUDENT ("
                + "$COLUMN_ID_ATTENDANCE_STUDENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_ATTENDANCE_DAY TEXT NOT NULL,"
                + "$COLUMN_STUDENT_ID INTEGER NOT NULL,"
                + "$COLUMN_ATTENDANCE_COURSE_ID INTEGER NOT NULL,"
                + "$COLUMN_REFERENCE_ID INTEGER NOT NULL,"
                + "$COLUMN_ATTENDANCE_STUDENT_STATUS TEXT NOT NULL,"
                + "FOREIGN KEY($COLUMN_STUDENT_ID) REFERENCES $TABLE_NAME_STUDENT($COLUMN_ID_STUDENT),"
                + "FOREIGN KEY($COLUMN_REFERENCE_ID) REFERENCES $TABLE_NAME_REFERENCE($COLUMN_ID_REFERENCE));")
        db.execSQL(createTableAttendanceStudentQuery)

        val createTableReferencesQuery = ("CREATE TABLE $TABLE_NAME_REFERENCE ("
                + "$COLUMN_ID_REFERENCE INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_REFERENCE_DESCRIPTION TEXT NOT NULL,"
                + "$COLUMN_REFERENCE_OPENING_DATE TEXT NOT NULL,"
                + "$COLUMN_REFERENCE_CLOSING_DATE TEXT NOT NULL,"
                + "$COLUMN_REFERENCE_CLOSE INTEGER NOT NULL);")
        db.execSQL(createTableReferencesQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_COURSE")
        onCreate(db)
    }
}