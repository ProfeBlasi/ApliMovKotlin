package com.example.tpintegrador.ui.students

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Student
import com.example.tpintegrador.DataBase.Entities.StudentAdapter
import com.example.tpintegrador.DataBase.Repository.StudentRepository
import com.example.tpintegrador.Login.Login
import com.example.tpintegrador.MainActivity
import com.example.tpintegrador.databinding.FragmentStudentBinding
import com.example.tpintegrador.ui.courses.CoursesFragment.Companion.COURSE_ID
import com.example.tpintegrador.ui.courses.CoursesFragment.Companion.NAME_COURSE
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null

    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var edtNameStudent: EditText
    private lateinit var edtLastNameStudent: EditText
    private lateinit var fabAddStudent: FloatingActionButton
    private lateinit var layoutCreateStudent: ConstraintLayout
    private lateinit var recyclerViewStudent: RecyclerView
    private lateinit var studentsMap: HashMap<Long, Student>
    private var selectedStudent: Student? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fabAddStudent = binding.fabAddStudent
        layoutCreateStudent = binding.layoutCreateStudent
        edtNameStudent = binding.edtNameStudent
        edtLastNameStudent = binding.edtLastNameStudent
        val btnCreateStudent = binding.btnCreateStudent
        val btnCancelCreateStudent = binding.btnCancelCreateStudent
        recyclerViewStudent = binding.recyclerViewStudent
        sharedPreferences = requireContext().getSharedPreferences(Login.PREFERENCES, Context.MODE_PRIVATE)
        val nameCourse = sharedPreferences.getString(NAME_COURSE, null)
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.title = nameCourse
        val courseId = sharedPreferences.getString(COURSE_ID, null)
        dbHelper = DBHelper(requireContext().applicationContext)
        val studentRepository = StudentRepository(dbHelper)
        studentsMap = courseId?.let { studentRepository.getAllStudentsMap(it.toString()) } ?: HashMap()
        studentAdapter = studentsMap?.let { StudentAdapter(it) }!!
        recyclerViewStudent.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewStudent.adapter = studentAdapter
        fabAddStudent.setOnClickListener {
            fabAddStudent.visibility = View.GONE
            layoutCreateStudent.visibility = View.VISIBLE
            recyclerViewStudent.visibility = View.GONE
        }

        btnCancelCreateStudent.setOnClickListener {
            clearInputFields()
            showStudentList()
        }

        btnCreateStudent.setOnClickListener {
            val lastNameStudent = edtNameStudent.text.toString().trim()
            val nameStudent = edtLastNameStudent.text.toString().trim()
            if (lastNameStudent.isNotEmpty() && nameStudent.isNotEmpty()) {
                val student = courseId?.let { it1 ->
                    Student(lastNameStudent,nameStudent,"","","","","","","",
                        it1
                    )
                }
                val studentId = student?.let { it1 -> studentRepository.insertStudent(it1) }
                if (studentId == -1L) {
                    Toast.makeText(requireContext().applicationContext, "Error adding student", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext().applicationContext, "Please enter the student's first and last name", Toast.LENGTH_SHORT).show()
            }
            clearInputFields()
            showStudentList()
            studentsMap = courseId?.let { studentRepository.getAllStudentsMap(it.toString()) } ?: HashMap()
            studentAdapter = studentsMap?.let { StudentAdapter(it) }!!
            recyclerViewStudent.adapter = studentAdapter
        }

        studentAdapter.setOnItemClickListener { student ->
            selectedStudent = student
            val intent = Intent(requireContext(), StudentShowViewModel::class.java)
            intent.putExtra("EXTRA_STUDENT", selectedStudent)
            startActivity(intent)
        }

        return root
    }
    private fun clearInputFields() {
        edtNameStudent.text.clear()
        edtLastNameStudent.text.clear()
    }
    private fun showStudentList() {
        fabAddStudent.visibility = View.VISIBLE
        layoutCreateStudent.visibility = View.GONE
        recyclerViewStudent.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
