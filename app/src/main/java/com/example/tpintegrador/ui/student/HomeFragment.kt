package com.example.tpintegrador.ui.student

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Course
import com.example.tpintegrador.DataBase.Entities.CourseAdapter
import com.example.tpintegrador.DataBase.Repository.CourseRepository
import com.example.tpintegrador.Login.Login
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import android.Manifest

class HomeFragment : Fragment() {
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
    private lateinit var btnGeolocation: Button
    private lateinit var geocoder: Geocoder

    companion object {
        var COURSE_ID = "COURSE_ID"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val dbHelper = DBHelper(requireContext())
        val courseRepository = CourseRepository(dbHelper)
        fabAddCourse = root.findViewById(R.id.fabAddCourse)
        layoutCreateCourse = root.findViewById(R.id.layoutCreateCourse)
        edtNameCourse = root.findViewById(R.id.edtNameCourse)
        edtSchoolCourse = root.findViewById(R.id.edtSchoolCourse)
        edtShiftCourse = root.findViewById(R.id.edtShiftCourse)
        edtAddressCourse = root.findViewById(R.id.edtAddressCourse)
        btnCreateCourse = root.findViewById(R.id.btnCreateCourse)
        btnCancelCreateCourse = root.findViewById(R.id.btnCancelCreateCourse)
        recyclerView = root.findViewById(R.id.recyclerViewCourses)
        firebaseAuth = FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val userId: String = requireActivity().intent.extras?.getString(Login.USER_ID).orEmpty()
        val courses = courseRepository.getAllCourses(userId)
        adapter = CourseAdapter(courses.toMutableList())
        recyclerView.adapter = adapter
        val updatedCourses = courseRepository.getAllCourses(userId)
        adapter.updateCourses(updatedCourses)
        geocoder = Geocoder(requireContext())
        btnGeolocation = root.findViewById(R.id.btnGeolocation)

        fabAddCourse.setOnClickListener {
            fabAddCourse.visibility = View.GONE
            layoutCreateCourse.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        fabAddCourse.setOnLongClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.logOutSession))
                .setPositiveButton(getString(R.string.logOut)) { dialogInterface: DialogInterface, i: Int ->
                    firebaseAuth.signOut()
                    val intent = Intent(requireContext(), Login::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
            true
        }

        btnCancelCreateCourse.setOnClickListener {
            cleanAndHide()
        }

        btnGeolocation.setOnClickListener {
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val address = getAddressFromLocation(latitude, longitude)
                    edtAddressCourse.setText(address)
                }
            }
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
                    Toast.makeText(requireContext(), getString(R.string.failedCourse), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.enterName), Toast.LENGTH_SHORT).show()
            }

            val updatedCourses = courseRepository.getAllCourses(userId)
            adapter.updateCourses(updatedCourses)
            cleanAndHide()
        }

        adapter.setOnCourseLongClickListener { course ->
            AlertDialog.Builder(requireContext())
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
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra(COURSE_ID, courseId)
            startActivity(intent)
        }

        return root
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

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>

        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            val street = address.thoroughfare
            val number = address.subThoroughfare

            if (street != null && number != null) {
                return "$street $number"
            }
        }
        return "No street and number found"
    }
}
