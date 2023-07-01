package com.example.tpintegrador.ui.courses

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
import androidx.appcompat.app.AlertDialog
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
import android.content.SharedPreferences
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CoursesFragment : Fragment() {
    private lateinit var fabAddCourse: FloatingActionButton
    private lateinit var layoutCreateCourse: androidx.cardview.widget.CardView
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
    private lateinit var coursesSelected: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewContainer: androidx.cardview.widget.CardView
    private lateinit var mapView: MapView
    private lateinit var mapContainer: FrameLayout

    companion object {
        var COURSE_ID = "course_id"
        var NAME_COURSE = "name_course"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_courses, container, false)

        val dbHelper = DBHelper(requireContext())
        val courseRepository = CourseRepository(dbHelper)
        fabAddCourse = root.findViewById(R.id.fabAddCourse)
        layoutCreateCourse = root.findViewById(R.id.viewContainerCreateCourse)
        edtNameCourse = root.findViewById(R.id.edtNameCourse)
        edtSchoolCourse = root.findViewById(R.id.edtSchoolCourse)
        edtShiftCourse = root.findViewById(R.id.edtShiftCourse)
        edtAddressCourse = root.findViewById(R.id.edtAddressCourse)
        btnCreateCourse = root.findViewById(R.id.btnCreateCourse)
        btnCancelCreateCourse = root.findViewById(R.id.btnCancelCreateCourse)
        recyclerView = root.findViewById(R.id.recyclerViewCourses)
        coursesSelected = root.findViewById(R.id.coursesSelected)
        viewContainer = root.findViewById(R.id.viewContainer)
        firebaseAuth = FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val mainActivity = requireActivity() as MainActivity
        val userId: String = mainActivity.getUserId()
        val courses = courseRepository.getAllCourses(userId)
        adapter = CourseAdapter(courses.toMutableList())
        recyclerView.adapter = adapter
        val updatedCourses = courseRepository.getAllCourses(userId)
        adapter.updateCourses(updatedCourses)
        geocoder = Geocoder(requireContext())
        btnGeolocation = root.findViewById(R.id.btnGeolocation)
        sharedPreferences = requireContext().getSharedPreferences(Login.PREFERENCES, Context.MODE_PRIVATE)
        mapView = root.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapContainer = root.findViewById(R.id.mapContainer)
        val nameCourse = sharedPreferences.getString(CoursesFragment.NAME_COURSE, null)
        if(nameCourse != null) coursesSelected.text = "Selected course " + nameCourse
        fabAddCourse.setOnClickListener {
            fabAddCourse.visibility = View.GONE
            layoutCreateCourse.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            viewContainer.visibility = View.GONE
        }

        btnCancelCreateCourse.setOnClickListener {
            cleanAndHide()
        }

        btnGeolocation.setOnClickListener {
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var latitude: Double = 0.0
            var longitude: Double = 0.0
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
                    latitude = location.latitude
                    longitude = location.longitude
                    val address = getAddressFromLocation(latitude, longitude)
                    edtAddressCourse.setText(address)
                }
            }

            mapContainer.visibility = View.VISIBLE
            mapView.getMapAsync { googleMap ->
                googleMap.clear()
                val markerOptions = MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .title("Current location")
                googleMap.addMarker(markerOptions)
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(14f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

        btnCreateCourse.setOnClickListener {
            val nameCourse = edtNameCourse.text.toString().trim()
            val schoolCourse = edtSchoolCourse.text.toString().trim()
            val shiftCourse = edtShiftCourse.text.toString().trim()
            val addressCourse = edtAddressCourse.text.toString().trim()

            if (nameCourse.isNotEmpty() && schoolCourse.isNotEmpty()) {
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
            coursesSelected.text = "Selected course " + course.name + " of " + course.school
            val editor = sharedPreferences.edit()
            editor.putString(COURSE_ID, course.id.toString())
            editor.putString(NAME_COURSE, course.name + " of " + course.school)
            editor.apply()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun cleanAndHide() {
        edtNameCourse.text.clear()
        edtSchoolCourse.text.clear()
        edtShiftCourse.text.clear()
        edtAddressCourse.text.clear()
        fabAddCourse.visibility = View.VISIBLE
        layoutCreateCourse.visibility = View.GONE
        mapContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        viewContainer.visibility = View.VISIBLE
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
