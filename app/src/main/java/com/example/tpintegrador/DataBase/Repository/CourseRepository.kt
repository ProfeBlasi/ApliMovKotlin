package com.example.tpintegrador.DataBase.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Course

class CourseRepository(private val dbHelper: DBHelper) {

    fun insertCourse(course: Course): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_NAME_COURSE, course.name)
        contentValues.put(DBHelper.COLUMN_NAME_SCHOOL, course.school)
        contentValues.put(DBHelper.COLUMN_NAME_SHIFT, course.shift)
        contentValues.put(DBHelper.COLUMN_NAME_ADDRESS, course.address)
        contentValues.put(DBHelper.COLUMN_NAME_USER_ID, course.userId)

        return db.insert(DBHelper.TABLE_NAME_COURSE, null, contentValues)
    }

    fun updateCourse(course: Course): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_NAME_COURSE, course.name)
        contentValues.put(DBHelper.COLUMN_NAME_SCHOOL, course.school)
        contentValues.put(DBHelper.COLUMN_NAME_SHIFT, course.shift)
        contentValues.put(DBHelper.COLUMN_NAME_ADDRESS, course.address)
        contentValues.put(DBHelper.COLUMN_NAME_USER_ID, course.userId)

        return db.update(
            DBHelper.TABLE_NAME_COURSE, contentValues, "${DBHelper.COLUMN_ID_COURSE} = ?",
            arrayOf(course.id.toString())
        )
    }

    fun deleteCourse(courseId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DBHelper.TABLE_NAME_COURSE, "${DBHelper.COLUMN_ID_COURSE} = ?", arrayOf(courseId.toString()))
    }

    @SuppressLint("Range")
    fun getAllCourses(currentUserId: String): List<Course> {
        val courses = mutableListOf<Course>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_COURSE} WHERE ${DBHelper.COLUMN_NAME_USER_ID} = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(currentUserId))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_COURSE))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_COURSE))
                val school = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_SCHOOL))
                val shift = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_SHIFT))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_ADDRESS))
                val userId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME_USER_ID))

                val course = Course(id, name, school, shift, address, userId)
                courses.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courses
    }
}
