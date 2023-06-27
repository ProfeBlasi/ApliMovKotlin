package com.example.tpintegrador.DataBase.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student

class StudentRepository(private val dbHelper: DBHelper) {

    fun insertStudent(student: Student): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_LAST_NAME_STUDENT, student.lastName)
        contentValues.put(DBHelper.COLUMN_NAME_STUDENT, student.nameStudent)
        contentValues.put(DBHelper.COLUMN_ADDRESS_STUDENT, student.address)
        contentValues.put(DBHelper.COLUMN_PHONE_STUDENT, student.phone)
        contentValues.put(DBHelper.COLUMN_NATIONALITY_STUDENT, student.nationality)
        contentValues.put(DBHelper.COLUMN_BIRTHDATE_STUDENT, student.birthdate)
        contentValues.put(DBHelper.COLUMN_DOCUMENT_STUDENT, student.document)
        contentValues.put(DBHelper.COLUMN_EMAIL_STUDENT, student.email)
        contentValues.put(DBHelper.COLUMN_STATE_STUDENT, student.state)
        contentValues.put(DBHelper.COLUMN_COURSE_ID_STUDENT, student.courseId)
        return db.insert(DBHelper.TABLE_NAME_STUDENT, null, contentValues)
    }

    fun updateStudent(student: Student): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_LAST_NAME_STUDENT, student.lastName)
        contentValues.put(DBHelper.COLUMN_NAME_STUDENT, student.nameStudent)
        contentValues.put(DBHelper.COLUMN_ADDRESS_STUDENT, student.address)
        contentValues.put(DBHelper.COLUMN_PHONE_STUDENT, student.phone)
        contentValues.put(DBHelper.COLUMN_NATIONALITY_STUDENT, student.nationality)
        contentValues.put(DBHelper.COLUMN_BIRTHDATE_STUDENT, student.birthdate)
        contentValues.put(DBHelper.COLUMN_DOCUMENT_STUDENT, student.document)
        contentValues.put(DBHelper.COLUMN_EMAIL_STUDENT, student.email)
        contentValues.put(DBHelper.COLUMN_STATE_STUDENT, student.state)
        contentValues.put(DBHelper.COLUMN_COURSE_ID_STUDENT, student.courseId)
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
    fun getAllStudentsMap(currentCourseId: String): HashMap<Long, Student> {
        val studentsMap = HashMap<Long, Student>()
        val db = dbHelper.readableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_STUDENT} WHERE ${DBHelper.COLUMN_COURSE_ID_STUDENT} = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(currentCourseId))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_STUDENT))
                val lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LAST_NAME_STUDENT))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_STUDENT))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS_STUDENT))
                val phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE_STUDENT))
                val nationality = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NATIONALITY_STUDENT))
                val birthdate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BIRTHDATE_STUDENT))
                val document = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DOCUMENT_STUDENT))
                val email = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL_STUDENT))
                val state = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_STATE_STUDENT))
                val courseId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COURSE_ID_STUDENT))

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
                studentsMap[id] = student
            } while (cursor.moveToNext())
        }
        cursor.close()
        return studentsMap
    }

    @SuppressLint("Range")
    fun getStudentById(studentId: Long): Student? {
        val db = dbHelper.readableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_STUDENT} WHERE ${DBHelper.COLUMN_ID_STUDENT} = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(studentId.toString()))

        if (cursor.moveToFirst()) {
            val lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LAST_NAME_STUDENT))
            val name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_STUDENT))
            val address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS_STUDENT))
            val phone = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE_STUDENT))
            val nationality = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NATIONALITY_STUDENT))
            val birthdate = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BIRTHDATE_STUDENT))
            val document = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DOCUMENT_STUDENT))
            val email = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL_STUDENT))
            val state = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_STATE_STUDENT))
            val courseId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COURSE_ID_STUDENT))

            val student = Student(
                studentId,
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

            cursor.close()
            return student
        }

        cursor.close()
        return null
    }
}
