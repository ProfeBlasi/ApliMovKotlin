package com.example.tpintegrador

import android.content.DialogInterface
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth

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
    private lateinit var firebaseAuth: FirebaseAuth

    companion object{
        var COURSE_ID = "COURSE_ID"
    }

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
        firebaseAuth = FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val userId: String = intent.extras?.getString(Login.USER_ID).orEmpty()
        val courses = courseRepository.getAllCourses(userId)
        adapter = CourseAdapter(courses.toMutableList())
        recyclerView.adapter = adapter
        val updatedCourses = courseRepository.getAllCourses(userId)
        adapter.updateCourses(updatedCourses)

        fabAddCourse.setOnClickListener {
            fabAddCourse.visibility = View.GONE
            layoutCreateCourse.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        fabAddCourse.setOnLongClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.logOutSession))
                .setPositiveButton(getString(R.string.logOut)) { dialogInterface: DialogInterface, i: Int ->
                    firebaseAuth.signOut()
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
            true
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
                    Toast.makeText(this, getString(R.string.failedCourse), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.enterName), Toast.LENGTH_SHORT).show()
            }

            val updatedCourses = courseRepository.getAllCourses(userId)
            adapter.updateCourses(updatedCourses)
            cleanAndHide()
        }

        adapter.setOnCourseLongClickListener { course ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.deleteCourse))
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    courseRepository.deleteCourse(course.id)
                    val updatedCourses = courseRepository.getAllCourses(userId)
                    adapter.updateCourses(updatedCourses)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        adapter.setOnCourseClickListener { course ->
            val courseId = course.id.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(COURSE_ID, courseId)
            startActivity(intent)
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
    override fun onBackPressed() {
        Toast.makeText(this, getString(R.string.you_are_already_logged), Toast.LENGTH_SHORT).show()
    }
}
