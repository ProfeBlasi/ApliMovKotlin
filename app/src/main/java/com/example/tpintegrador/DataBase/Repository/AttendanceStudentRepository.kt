package com.example.tpintegrador.DataBase.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.AttendanceStudent
import com.example.tpintegrador.ui.asistencia.AttendanceStatus
import com.example.tpintegrador.ui.asistencia.StudentDto
import java.util.*

class AttendanceStudentRepository(private val dbHelper: DBHelper) {

    fun insertAttendanceStudent(attendanceStudent: AttendanceStudent): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_DAY, attendanceStudent.attendanceDay)
        contentValues.put(DBHelper.COLUMN_STUDENT_ID, attendanceStudent.studentId)
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_COURSE_ID, attendanceStudent.courseId)
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_STUDENT_STATUS, attendanceStudent.attendanceStatus)
        contentValues.put(DBHelper.COLUMN_REFERENCE_ID, attendanceStudent.referenceId)

        return db.insert(DBHelper.TABLE_NAME_ATTENDANCE_STUDENT, null, contentValues)
    }

    fun updateAttendanceStudent(attendanceStudent: AttendanceStudent): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_DAY, attendanceStudent.attendanceDay)
        contentValues.put(DBHelper.COLUMN_STUDENT_ID, attendanceStudent.studentId)
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_COURSE_ID, attendanceStudent.courseId)
        contentValues.put(DBHelper.COLUMN_ATTENDANCE_STUDENT_STATUS, attendanceStudent.attendanceStatus)
        contentValues.put(DBHelper.COLUMN_REFERENCE_ID, attendanceStudent.referenceId)

        return db.update(
            DBHelper.TABLE_NAME_ATTENDANCE_STUDENT, contentValues, "${DBHelper.COLUMN_ID_ATTENDANCE_STUDENT_ID} = ?",
            arrayOf(attendanceStudent.id.toString())
        )
    }

    fun deleteAttendanceStudent(attendanceStudentId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DBHelper.TABLE_NAME_ATTENDANCE_STUDENT, "${DBHelper.COLUMN_ID_ATTENDANCE_STUDENT_ID} = ?", arrayOf(attendanceStudentId.toString()))
    }

    @SuppressLint("Range")
    fun getAllAttendanceStudents(): List<AttendanceStudent> {
        val attendanceStudents = mutableListOf<AttendanceStudent>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_ATTENDANCE_STUDENT}"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_ATTENDANCE_STUDENT_ID))
                val attendanceDay = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ATTENDANCE_DAY))
                val studentId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_STUDENT_ID))
                val courseId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ATTENDANCE_COURSE_ID))
                val status = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ATTENDANCE_STUDENT_STATUS))
                val referenceId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_REFERENCE_ID))

                val attendanceStudent = AttendanceStudent(id, attendanceDay, studentId, courseId, status, referenceId)
                attendanceStudents.add(attendanceStudent)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return attendanceStudents
    }

    @SuppressLint("Range")
    fun getAttendanceStudentsByDay(attendanceDay: String): List<StudentDto> {
        val attendanceStudents = mutableListOf<StudentDto>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_ATTENDANCE_STUDENT} WHERE ${DBHelper.COLUMN_ATTENDANCE_DAY} = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(attendanceDay))
        if (cursor.moveToFirst()) {
            do {
                val studentId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_STUDENT_ID))
                val attendanceId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_ATTENDANCE_STUDENT_ID))
                val status = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ATTENDANCE_STUDENT_STATUS))
                val studentDto = StudentDto(
                    id = studentId,
                    attendanceId = attendanceId,
                    lastName = "",
                    nameStudent = "",
                    attendanceStatus = AttendanceStatus.valueOf(status)
                )
                attendanceStudents.add(studentDto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return attendanceStudents
    }
}
