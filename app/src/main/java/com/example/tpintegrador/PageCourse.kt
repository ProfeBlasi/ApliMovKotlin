package com.example.tpintegrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Course
import com.example.tpintegrador.DataBase.Entities.CourseAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.tpintegrador.DataBase.Repository.CourseRepository
import com.example.tpintegrador.Login.Login

class PageCourse : AppCompatActivity() {
    private lateinit var fabAddCourse: FloatingActionButton
    private lateinit var layoutCreateCourse: ConstraintLayout
    private lateinit var edtNameCourse: EditText
    private lateinit var edtSchoolCourse: EditText
    private lateinit var edtShiftCourse: EditText
    private lateinit var edtAddressCourse: EditText
    private lateinit var btnCreateCourse: Button
    private lateinit var btnCancelCreateCourse: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_course)
        val dbHelper = DBHelper(baseContext)
        val courseRepository = CourseRepository(dbHelper)
        fabAddCourse = findViewById(R.id.fabAddCourse)
        layoutCreateCourse = findViewById(R.id.layoutCreateCourse)
        edtNameCourse = findViewById(R.id.edtNameCourse)
        edtSchoolCourse = findViewById(R.id.edtSchoolCourse)
        edtShiftCourse = findViewById(R.id.edtShiftCourse)
        edtAddressCourse = findViewById(R.id.edtAddressCourse)
        btnCreateCourse = findViewById(R.id.btnCreateCourse)
        btnCancelCreateCourse = findViewById(R.id.btnCancelCreateCourse)
        recyclerView = findViewById(R.id.recyclerViewCourses)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val userId: String = intent.extras?.getString(Login.USER_ID).orEmpty()
        val courses = courseRepository.getAllCourses()
        adapter = CourseAdapter(courses.toMutableList())
        recyclerView.adapter = adapter

        fabAddCourse.setOnClickListener {
            fabAddCourse.visibility = View.GONE
            layoutCreateCourse.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        btnCancelCreateCourse.setOnClickListener {
            cleanAndHide()
        }

        btnCreateCourse.setOnClickListener {
            val nameCourse = edtNameCourse.text.toString().trim()
            val schoolCourse = edtSchoolCourse.text.toString().trim()
            val shiftCourse = edtShiftCourse.text.toString().trim()
            val addressCourse = edtAddressCourse.text.toString().trim()

            if (nameCourse.isNotEmpty()) {
                val course = Course(nameCourse, schoolCourse, shiftCourse, addressCourse, userId)
                val courseId = courseRepository.insertCourse(course)
                if (courseId == -1L) {
                    Toast.makeText(this, getString(R.string.failedCourse), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, getString(R.string.enterName), Toast.LENGTH_SHORT).show()
            }

            val updatedCourses = courseRepository.getAllCourses()
            adapter.updateCourses(updatedCourses)
            cleanAndHide()
        }
    }

    private fun cleanAndHide() {
        edtNameCourse.text.clear()
        edtSchoolCourse.text.clear()
        edtShiftCourse.text.clear()
        edtAddressCourse.text.clear()
        fabAddCourse.visibility = View.VISIBLE
        layoutCreateCourse.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}
