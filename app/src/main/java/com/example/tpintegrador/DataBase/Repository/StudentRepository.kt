package com.example.tpintegrador.DataBase.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student

class StudentRepository(private val dbHelper: DBHelper) {
    fun addStudent(student: Student): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_LAST_NAME, student.lastName)
        contentValues.put(DBHelper.COLUMN_NAME, student.name)
        contentValues.put(DBHelper.COLUMN_ADDRESS, student.address)
        contentValues.put(DBHelper.COLUMN_PHONE, student.phone)
        contentValues.put(DBHelper.COLUMN_NATIONALITY, student.nationality)
        contentValues.put(DBHelper.COLUMN_BIRTHDATE, student.birthdate)
        contentValues.put(DBHelper.COLUMN_DOCUMENT, student.document)
        contentValues.put(DBHelper.COLUMN_EMAIL, student.email)
        contentValues.put(DBHelper.COLUMN_STATE, student.state)
        contentValues.put(DBHelper.COLUMN_COURSE_ID, student.courseId)
        return db.insert(DBHelper.TABLE_NAME_STUDENT, null, contentValues)
    }

    fun updateStudent(student: Student): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_LAST_NAME, student.lastName)
        contentValues.put(DBHelper.COLUMN_NAME, student.name)
        contentValues.put(DBHelper.COLUMN_ADDRESS, student.address)
        contentValues.put(DBHelper.COLUMN_PHONE, student.phone)
        contentValues.put(DBHelper.COLUMN_NATIONALITY, student.nationality)
        contentValues.put(DBHelper.COLUMN_BIRTHDATE, student.birthdate)
        contentValues.put(DBHelper.COLUMN_DOCUMENT, student.document)
        contentValues.put(DBHelper.COLUMN_EMAIL, student.email)
        contentValues.put(DBHelper.COLUMN_STATE, student.state)
        contentValues.put(DBHelper.COLUMN_COURSE_ID, student.courseId)
        return db.update(
            DBHelper.TABLE_NAME_STUDENT, contentValues, "${DBHelper.COLUMN_ID_STUDENT} = ?",
            arrayOf(student.id.toString())
        )
    }

    fun deleteStudent(studentId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DBHelper.TABLE_NAME_STUDENT, "${DBHelper.COLUMN_ID_STUDENT} = ?", arrayOf(studentId.toString()))
    }

    @SuppressLint("Range")
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = dbHelper.readableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_STUDENT}"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_STUDENT))
                val lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LAST_NAME))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS))
                val phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE))
                val nationality = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NATIONALITY))
                val birthdate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BIRTHDATE))
                val document = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DOCUMENT))
                val email = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL))
                val state = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_STATE))
                val courseId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COURSE_ID))

                val student = Student(
                    id,
                    lastName,
                    name,
                    address,
                    phone,
                    nationality,
                    birthdate,
                    document,
                    email,
                    state,
                    courseId
                )
                students.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return students
    }
}